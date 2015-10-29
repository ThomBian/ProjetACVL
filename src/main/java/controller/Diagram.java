package controller;

import javax.swing.UIManager;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import model.CompositeState;
import model.FinalState;
import model.InitialState;
import model.SimpleState;
import model.StandardTransition;
import model.State;
import model.Transition;
import view.CustomMxGraph;
import view.MainView;
import view.Style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Diagram {

	private static final Diagram instance = new Diagram();

	public static Diagram getInstance() {
		return instance;
	}

	private MainView mainView;
	private mxGraph graph;
	private Set<State> directSons = new HashSet<State>();
	private List<DiagramError> errors;
	private Map<State, mxCell> linkedStates = new HashMap<State, mxCell>();
	private Map<Transition, mxCell> linkedTransitions = new HashMap<Transition, mxCell>();
	
	public State getStateFromMxCell(Object cell){
		for (State o : linkedStates.keySet()) {
		    if (linkedStates.get(o).equals(cell)) {
		      return o;
		    }
		}
		return null;
    }

	private Diagram() {
		// todo move the custom mx graph to a view class (graphview)
		graph = new CustomMxGraph();
		graph.setAllowDanglingEdges(false);
		graph.setConnectableEdges(false);
		graph.setDropEnabled(true);
        errors = new ArrayList<>();
	}

	public void createInitialState() {
		// System.out.println(graph.getSelectionCell());
		State s = new InitialState();
		directSons.add(s);
		// TODO : call to graphview instead of direct class mxgraph object
		mxCell vertex = (mxCell) graph.createVertex(graph.getDefaultParent(), null, "", 20, 20, 30, 30, Style.INITIAL);
		graph.addCell(vertex);
		linkedStates.put(s, vertex);
	}

	public void createState(String name) {
		// TODO verify unicity of name
		State s = new SimpleState(name);
		directSons.add(s);
		// TODO : call to graphview instead of direct class mxgraph object
		mxCell vertex = (mxCell) graph.createVertex(graph.getDefaultParent(), null, name, 20, 20, 80, 30, Style.STATE);
		graph.addCell(vertex);
		linkedStates.put(s, vertex);
	}

	public void createCompositeState(String name) {
		// TODO verify unicity of name
		State s = new CompositeState(name);
		directSons.add(s);
		// TODO : call to graphview instead of direct class mxgraph object
		mxCell vertex = (mxCell) graph.createVertex(graph.getDefaultParent(), null, name, 20, 20, 150, 180,
				Style.COMPOSITE);
		graph.addCell(vertex);
		linkedStates.put(s, vertex);
	}
	
	// c == null => first level state
	// old == null => was a first level state
	public void dropStateIntoCompositeState(State s, CompositeState c) {
		// removing possible existing link to parent
		CompositeState parent = findParentState(s);
		if (parent != null && c != null) {
			parent.getStates().remove(s);
			// add new link
			c.getStates().add(s);
		}else if(parent != null && c == null){
			parent.getStates().remove(s);
			// add new link
			directSons.add(s);
		}else{
			this.directSons.remove(s);
			// add new link
			c.getStates().add(s);
		}

	}
	private void removeTransitionFromTarget(State target){
		List<Transition> removed;
		for(State s : directSons){
			removed = s.removeTransitionInSonsFromTarget(target);
			for(Transition tr : removed){
				linkedTransitions.remove(tr);
			}
		}
	}
	public void removeState(State s) {
		// remove transitions linked to this state
		removeTransitionFromTarget(s);
		linkedStates.remove(s);
		if(s.isCompositeState()){
			List<State> sons = ((CompositeState)s).getAllStates();
			for(State son : sons){
				linkedStates.remove(son);
			}
		}
		CompositeState parent = findParentState(s);
		if(parent == null){
			directSons.remove(s);
		}else{
			parent.getStates().remove(s);
		}
	}

	public void launchApplication() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			mainView = new MainView(graph);
			mainView.getFrame().setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createFinalState() {
		State s = new FinalState();
		directSons.add(s);
		// TODO : call to graphview instead of direct class mxgraph object
		mxCell vertex = (mxCell) graph.createVertex(graph.getDefaultParent(), null, "", 20, 20, 30, 30, Style.FINAL);
		graph.addCell(vertex);
		linkedStates.put(s, vertex);
	}

	public void validate() {
		mainView.displayValidationWindow(errors);
		System.out.println();
		System.out.println(this.toString());
	}

	public void addError(DiagramError e) {
		errors.add(e);
	}

	private CompositeState findParentState(State s) {
		CompositeState result = null;
		for (State cur : directSons) {
			if (s.equals(cur))
				return result;

			if (cur.isCompositeState()) {
				result = ((CompositeState) cur).findParentState(s);
				if (result != null)
					return result;
			}
		}
		return result;
	}

	public String toString(){
		String result = new String();
		for(State s :directSons){
			result += s.toString() + "\n";
		}
		return result;
	}

	
	// TODO Do not pass mxCell object as parameter in this method, this IS NOT MODULAR
	public void addTransitionToModel(State sourceState, State targetState, mxCell transition) {
		Transition t;
		if(sourceState.isInitialState()){
			t = new Transition<InitialState>();
		}else{
			t = new StandardTransition();
		}
		sourceState.getOutgoingTransitions().add(t);
		t.setDestination(targetState);
		t.setSource(sourceState);

		linkedTransitions.put(t, transition);
	}
	
	public void removeTransitionFromModel(Transition transition){
		linkedTransitions.remove(transition);
		// Remove all occurence of this transition in all possible outgoingTransitions !
		for(State s : directSons){
			if(s.removeTransitionInSons(transition))
				break;
		}
		
	}

	public Transition getTransitionFromMxCell(mxCell cell) {
		for (Transition o : linkedTransitions.keySet()) {
		    if (linkedTransitions.get(o).equals(cell)) {
		      return o;
		    }
		}
		return null;
	}
}
