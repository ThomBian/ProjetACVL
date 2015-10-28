package controller;

import javax.swing.UIManager;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import model.CompositeState;
import model.FinalState;
import model.InitialState;
import model.SimpleState;
import model.State;
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
	private Map<State, mxCell> link = new HashMap<State, mxCell>();

	public State getStateFromMxCell(Object cell){
		for (State o : link.keySet()) {
		    if (link.get(o).equals(cell)) {
		      return o;
		    }
		}
		return null;
    }

	private Diagram() {
		// todo move anonym class to a true class 
		graph = new mxGraph(){
			  // Make all edges unmovable
			  public boolean isCellMovable(Object cell)
			  {
			    return !getModel().isEdge(cell);
			  }	
			  public boolean isValidDropTarget(Object cell, Object[] cells){
				  // TODO : if cell  = null OK
				  if (cells.length == 1 && ((mxCell)cell).isVertex() && ((mxCell)cell).getStyle().equals(Style.COMPOSITE))
				  {	  
					  return true;
				  }
				  return false;
			  }
		};
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
		link.put(s, vertex);
	}

	public void createState(String name) {
		// TODO verify unicity of name
		State s = new SimpleState(name);
		directSons.add(s);
		// TODO : call to graphview instead of direct class mxgraph object
		mxCell vertex = (mxCell) graph.createVertex(graph.getDefaultParent(), null, name, 20, 20, 80, 30, Style.STATE);
		graph.addCell(vertex);
		link.put(s, vertex);
	}

	public void createCompositeState(String name) {
		// TODO verify unicity of name
		State s = new CompositeState(name);
		directSons.add(s);
		// TODO : call to graphview instead of direct class mxgraph object
		mxCell vertex = (mxCell) graph.createVertex(graph.getDefaultParent(), null, name, 20, 20, 150, 180,
				Style.COMPOSITE);
		graph.addCell(vertex);
		link.put(s, vertex);
	}
	
	// c == null => first level state
	// old == null => was a first level state
	public void dropStateIntoCompositeState(State s, CompositeState c) {
		// removing possible existing link to parent
		CompositeState old = findParentState(s);
		System.out.println("Drop cell "+s+" from "+old+ " into "+c);
		if (old != null && c != null) {
			old.getStates().remove(s);
			// add new link
			c.getStates().add(s);
		}else if(old != null && c == null){
			old.getStates().remove(s);
			// add new link
			directSons.add(s);
		}else{
			this.directSons.remove(s);
			// add new link
			c.getStates().add(s);
		}

	}

	public void removeState(State s) {

		// TODO search state

		// TODO remove all link to this state ie. parent state & transitions

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
		link.put(s, vertex);
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
}
