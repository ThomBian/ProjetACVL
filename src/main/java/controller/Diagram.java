package controller;

import javax.swing.UIManager;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.view.mxGraph;

import model.CompositeState;
import model.FinalState;
import model.InitialState;
import model.InitialTransition;
import model.NamedState;
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
        errors = new ArrayList<>();
	}

	public void createInitialState() {
		State s = new InitialState();
		directSons.add(s);
		// TODO : call to graphview instead of direct class mxgraph object
		mxCell vertex = (mxCell) mainView.getGraph().getGraph().createVertex(mainView.getGraph().getGraph().getDefaultParent(), null, "", 20, 20, 30, 30, Style.INITIAL);
		mainView.getGraph().getGraph().addCell(vertex);
		linkedStates.put(s, vertex);
	}

	public void createState(String name) {
		if (!verifyName(name)) {
			name = changeName(name);
			System.out.println("verified : " + name);
		}
		State s = new SimpleState(name);
		directSons.add(s);
		// TODO : call to graphview instead of direct class mxgraph object
		mxCell vertex = (mxCell) mainView.getGraph().getGraph().createVertex(mainView.getGraph().getGraph().getDefaultParent(), null, name, 20, 20, 80, 30, Style.STATE);
		mainView.getGraph().getGraph().addCell(vertex);
		linkedStates.put(s, vertex);
	}

	public void createCompositeState(String name) {
		if (!verifyName(name)) {
			name = changeName(name);
		}
		State s = new CompositeState(name);
		directSons.add(s);
		// TODO : call to graphview instead of direct class mxgraph object
		mxCell vertex = (mxCell) mainView.getGraph().getGraph().createVertex(mainView.getGraph().getGraph().getDefaultParent(), null, name, 20, 20, 150, 180,
				Style.COMPOSITE);
		mainView.getGraph().getGraph().addCell(vertex);
		linkedStates.put(s, vertex);
	}
	
	// c == null => first level state
	// old == null => was a first level state
	public void dropStateIntoCompositeState(State s, CompositeState c) {
		if(s == null ) return;
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
		}else if (parent == null && c != null){
			directSons.remove(s);
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
	/*
	 * Remove a state and the transitions leading to it
	 * If its a composite one, the sons are deleted and the transitions too
	 */
	public void removeState(State s) {
		// remove transitions linked to this state
		
		List<State> sonsAndFather = s.getAllStates();
		for(State son : sonsAndFather){
			removeTransitionFromTarget(son);
			for(Transition t : son.getOutgoingTransitions()){
				removeTransitionFromModel(t);
			}
			linkedStates.remove(son);
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
			mainView = new MainView();
			mainView.getFrame().setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createFinalState() {
		State s = new FinalState();
		directSons.add(s);
		// TODO : call to graphview instead of direct class mxgraph object
		mxCell vertex = (mxCell) mainView.getGraph().getGraph().createVertex(mainView.getGraph().getGraph().getDefaultParent(), null, "", 20, 20, 30, 30, Style.FINAL);
		mainView.getGraph().getGraph().addCell(vertex);
		linkedStates.put(s, vertex);
	}

	public void validate() {
        int nbInitialState = 0;
        InitialState input = null;
        for(State s: directSons){
            if (s instanceof InitialState) {
                nbInitialState++;
                input = (InitialState) s;
            }
        }
        if (nbInitialState == 0 || nbInitialState > 1){
            this.addError(new DiagramError("Erreur avec les états initiaux"));
            return;
        }
        if(input != null){
            input.setReach(true);
        }
        for(State s : linkedStates.keySet()){
            if (!s.isReach()){
                this.addError(new DiagramError("State unreachable : "+s.toString()));
            }
        }
		mainView.displayValidationWindow(errors);
		System.out.println();
		System.out.println(this.toString());
        for(State s: directSons){
            s.setReach(false);
        }
        errors.clear();
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
	
	private boolean verifyName(String name) {
		for (Map.Entry<State, mxCell> entry : linkedStates.entrySet()) {
			if (entry.getKey().getClass().equals(SimpleState.class) || entry.getKey().getClass().equals(CompositeState.class)) {
				if (name.equals(((NamedState) entry.getKey()).getName()))
					return false;
			}
		}
		return true;
	}


	private String changeName(String name) {
		int number = 2;
		while (true) {
			if (verifyName(name + " " + String.valueOf(number))) {
				return name + " " + String.valueOf(number);
			}
			number++;
		}
	}

	// TODO Do not pass mxCell object as parameter in this method, this IS NOT MODULAR
	public void addTransitionToModel(State sourceState, State targetState, mxCell transition) {
		Transition t;
		if(sourceState.isInitialState()){
			t = new Transition<InitialState>((InitialState)sourceState,targetState);
		}else{
			t = new StandardTransition(sourceState,targetState);
		}
		sourceState.getOutgoingTransitions().add(t);


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
	
	/*
	 * Flatten a VALID graph
	 */
	public void flatten() {
		// TODO Verify that the graph is Valid
		boolean isGraphValid = true;
		if(isGraphValid){

			
			Set<Transition> transitions = getAllTransitions();
			Set<Transition> trashOfTransitions = new HashSet<Transition>();
			Set<Transition> newTransitions = new HashSet<Transition>();
			State source, dest, newDest;
			Transition newTransition;
			// Search for transition that needs to be upgraded or created
			for(Transition t : transitions){
				
				source = t.getSource();
				dest = t.getDestination();
				if(t.getSource().isCompositeState() && ! t.getDestination().isCompositeState()  ){
	
					for(State s : ((CompositeState)source).getStates()){
						// We dont want to create transition from initial/final state here
						if( !s.isInitialState() && !s.isFinalState()){
							// Generate transitions and add it to the current source
							newTransition = new StandardTransition(s, dest);
							newTransitions.add(newTransition);
							s.getOutgoingTransitions().add(newTransition);
						}	
					}
					// Remove current transition 
					trashOfTransitions.add(t);
				}else if(t.getSource().isCompositeState() && t.getDestination().isCompositeState() ){
					
					for(State s : ((CompositeState)source).getStates()){
						// We dont want to create transition from initial/final state here
						if( !s.isInitialState() && !s.isFinalState()){
							InitialState init = ((CompositeState)t.getDestination()).getInitState();
							// Generate transitions and add it to the source
							for(Transition tInit : init.getOutgoingTransitions()){
								newTransition = new StandardTransition(s, tInit.getDestination());
								s.getOutgoingTransitions().add(newTransition);
								newTransitions.add(newTransition);
							}
						}
						
					}
					// Remove current transition 
					trashOfTransitions.add(t);
					
				}else if(!t.getSource().isCompositeState() && t.getDestination().isCompositeState()){
					
					InitialState init = ((CompositeState)t.getDestination()).getInitState();
					// Generate transitions and add it to the source
					for(Transition tInit : init.getOutgoingTransitions()){
						// TODO is transition destination is also composite state do something !!
						/* newDest = tInit.getDestination();
						while(newDest.isCompositeState()){
							newDest = ((CompositeState)t.getDestination()).getInitState();
						} 
						*/
						if(source.isInitialState()){
							newTransition = new InitialTransition((InitialState)source, tInit.getDestination());
						}else{
							newTransition = new StandardTransition(source, tInit.getDestination());
						}
						source.getOutgoingTransitions().add(newTransition);
						newTransitions.add(newTransition);
					}
					// Remove current transition 
					trashOfTransitions.add(t);
				}
			}
			mxCell[] edges = new mxCell[1];
			// handle trashOfTransitions ie. remove transitions
			for(Transition t : trashOfTransitions){
				edges[0] =  linkedTransitions.get(t);
				mainView.getGraph().getGraph().removeCells(edges);
				removeTransitionFromModel(t);
			}
			// Attach new transition to mxcell in linkedmap & make them appear
			for(Transition t : newTransitions){
				mxCell sourceCell = linkedStates.get(t.getSource()), destCell = linkedStates.get(t.getDestination()) ;
				mxCell edge = (mxCell) mainView.getGraph().getGraph().createEdge(mainView.getGraph().getGraph().getDefaultParent(), null, "", sourceCell, destCell, Style.EDGE);
				edge = (mxCell) mainView.getGraph().getGraph().addEdge(edge, mainView.getGraph().getGraph().getDefaultParent(), sourceCell, destCell, null);
				linkedTransitions.put(t, edge);
			}
			// get List of states to place in directSons & move them both graphically and in the directSosn set
			mxCell[] vertex = new mxCell[1];
			Set<State> misplacedStates = new HashSet<State>();
			
			// Retrieve all state that require to be moved 
			for(State s : directSons){
				if(s.isCompositeState()){
					// get simple & final states recursively
					misplacedStates.addAll(s.getSimpleFinalStateInSons());
				}	
			}
			// remove their reference in their parent
			for(State s : misplacedStates){
				CompositeState parent = findParentState(s);
				parent.getStates().remove(s);
			}
			// add them to the first level 
			directSons.addAll(misplacedStates);
			// Graphically place them at the first level (not in composite state anymore)
			// removing composite states ! 
			Set<CompositeState> toBeRemoved = new HashSet<CompositeState>();
			Object[] cells = new Object[1];
			Object[] sons = new Object[1];
			for(State s : getAllStates()){
				
				if(s.isCompositeState()){
					cells[0] = linkedStates.get(s);
					// graphically explode composite state
					for(State son : ((CompositeState)s).getStates()){
						if(son.isInitialState()){
							sons[0] = linkedStates.get(son);
							mainView.getGraph().getGraph().removeCells(sons);
							removeState(son);
						}
					}
					mainView.getGraph().getGraph().ungroupCells(cells);
					toBeRemoved.add((CompositeState) s);
					// removeState(s); // children will go away too with this ones
				}	
			}
			
			for(State s : toBeRemoved){
				removeState(s);
			}
			System.out.println();
			System.out.println(this.toString());
			mainView.getGraph().informUser("Operation performed !");
		}
		else{
			mainView.getGraph().informUser("The graph must be valid in order to perform this operation");
		}
	}
	
	/*
	 * Retrieve all states
	 */
	private List<State> getAllStates(){
		List<State> states = new ArrayList<State>();
		for(State s : directSons){
			states.addAll(s.getAllStates());
		}
		return states;
	}
	
	private Set<Transition> getAllTransitions(){
		Set<Transition> transitions = new HashSet<Transition>();
		for(State s : directSons){
			transitions.addAll(s.getAllTransitions());
		}
		return transitions;
	}
	
}
