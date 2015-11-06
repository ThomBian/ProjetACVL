package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.UIManager;

import com.mxgraph.model.mxCell;

import controller.visitor.FlattenVisitor;
import controller.visitor.StateDrawerVisitor;
import controller.visitor.ValidVisitor;
import model.Action;
import model.CompositeState;
import model.FinalState;
import model.Guard;
import model.InitialState;
import model.InitialTransition;
import model.NamedState;
import model.SimpleState;
import model.StandardTransition;
import model.State;
import model.Transition;
import view.CustomMxGraph;
import view.GraphView;
import view.MainView;
import view.Style;

public class Diagram {

	private static final Diagram instance = new Diagram();

	public static Diagram getInstance() {
		return instance;
	}

	private MainView mainView;
	private FlattenVisitor flattenVisitor = new FlattenVisitor();
	private ValidVisitor validVisitor = new ValidVisitor();
	private StateDrawerVisitor drawerVisitor = new StateDrawerVisitor();
	private Set<State> directSons = new HashSet<State>();
	
	

	private Diagram() {

	}

	public void createInitialState() {
		InitialState s = new InitialState();
		directSons.add(s);
		mainView.getGraph().insertState(s);
	}

	public void createState(String name) {
		if (!verifyName(name, getAllStates())) {
			name = changeName(name);
		}
		SimpleState s = new SimpleState(name);
		mainView.getGraph().insertState(s);
		directSons.add(s);
	}

	public void createCompositeState(String name) {
		if (!verifyName(name, getAllStates())) {
			name = changeName(name);
		}
		CompositeState s = new CompositeState(name);
		directSons.add(s);
		mainView.getGraph().insertState(s);
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
		}else if (parent == null && c != null){
			directSons.remove(s);
			// add new link
			c.getStates().add(s);
		}
	}

    public boolean validate() {
        DiagrammValidator validator = new DiagrammValidator();
        return validator.validate();
    }

    private void removeTransitionFromTarget(State target){
		List<Transition<State>> removed;
		for(State s : directSons){
			removed = s.removeTransitionInSonsFromTarget(target);
			for(Transition<State> tr : removed){
				getLinkedStates().remove(tr);
			}
		}
	}
    public Map<State, mxCell> getLinkedStates(){
    	return mainView.getGraph().getLinkedStates();
    }
    public Map<Transition<State>, mxCell> getLinkedTransitions(){
    	return mainView.getGraph().getLinkedTransitions();
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
			for(Transition<State> t : son.getOutgoingTransitions()){
				removeTransitionFromModel(t);
			}
			mainView.getGraph().getLinkedStates().remove(son);
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
		FinalState s = new FinalState();
		directSons.add(s);
		mainView.getGraph().insertState(s);
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
	
	private boolean verifyName(String name, List<State> states) {
		for (State s : states) {
			if (s.isNamedState()) {
				if (name.equals(((NamedState) s).getName())) {
					return false;
				}
			}
		}
		return true;
	}


	private String changeName(String name) {
		int number = 2;
		while (true) {
			if (verifyName(name + " " + String.valueOf(number), getAllStates())) {
				return name + " " + String.valueOf(number);
			}
			number++;
		}
	}

	// TODO Do not pass mxCell object as parameter in this method, this IS NOT MODULAR
	@SuppressWarnings("unchecked")
	public void addTransitionToModel(State sourceState, State targetState, mxCell transition) {
		Transition<?> t;
		if(sourceState.isInitialState()){
			t = new InitialTransition((InitialState)sourceState,targetState);
		}else{
			t = new StandardTransition(sourceState,targetState);
		}
		if (t instanceof StandardTransition)
			((StandardTransition)t).setGuard(new Guard("Default Guard"));
		sourceState.getOutgoingTransitions().add((Transition<State>) t);
		getLinkedTransitions().put((Transition<State>) t, transition);
		updateTransitionName((Transition<State>) t,"Default transition");
	}
	
	/*
	 * Remove this transition from everywhere (incl. states)
	 */
	public void removeTransitionFromModel(Transition<State> transition){
		getLinkedTransitions().remove(transition);
		// Remove all occurence of this transition in all possible outgoingTransitions !
		for(State s : directSons){
			if(s.removeTransitionInSons(transition))
				break;
		}
	}

	
	/*
	 * Flatten a VALID 
	 */
	@SuppressWarnings("unchecked")
	public void flatten() {
		// TODO Verify that the graph is Valid

		if(validate()){

			Set<Transition<State>> transitions = getAllTransitions();
			Set<Transition<State>> trashOfTransitions = new HashSet<Transition<State>>();
			Set<Transition<State>> newTransitions = new HashSet<Transition<State>>();
			State source, dest;
			Transition<? extends State> newTransition;
			// Search for transition that needs to be upgraded or created

			while(areSomeTransitionsLinkedToCompositeState()){

				transitions = getAllTransitions();
				for(Transition<State> t : transitions){
					
					source = t.getSource();
					dest = t.getDestination();
					if(t.getSource().isCompositeState() && ! t.getDestination().isCompositeState()  ){
		
						for(State s : ((CompositeState)source).getStates()){
							// We dont want to create transition from initial/final state here
							if( !s.isInitialState() && !s.isFinalState()){
								// Generate transitions and add it to the current source
								newTransition = new StandardTransition(s, dest);
								newTransitions.add((Transition<State>) newTransition);
								s.getOutgoingTransitions().add((Transition<State>) newTransition);
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
								for(Transition<State> tInit : init.getOutgoingTransitions()){
									newTransition = new StandardTransition(s, tInit.getDestination());
									s.getOutgoingTransitions().add((Transition<State>) newTransition);
									newTransitions.add((Transition<State>) newTransition);
								}
							}
							
						}
						// Remove current transition 
						trashOfTransitions.add(t);
						
					}else if(!t.getSource().isCompositeState() && t.getDestination().isCompositeState()){
						
						InitialState init = ((CompositeState)t.getDestination()).getInitState();
						// Generate transitions and add it to the source
						Transition<State> tInit = null ;
						for(Transition <State> tInitBis : init.getOutgoingTransitions()){
							tInit = tInitBis; 
							break;
						}
							
						if(source.isInitialState()){
							newTransition = new InitialTransition((InitialState)source, tInit.getDestination());
						}else{
							newTransition = new StandardTransition(source, tInit.getDestination());
						}
						source.getOutgoingTransitions().add((Transition<State>) newTransition);
						newTransitions.add((Transition<State>) newTransition);
					
						// Remove current transition 
						trashOfTransitions.add(t);
					}
				}
				// handle trashOfTransitions ie. remove transitions
				for(Transition<State> t : trashOfTransitions){	
					removeTransitionFromModel(t);
				}
			}
			
			// Attach new transition to mxcell in linkedmap & make them appear
			for(Transition<State> t : newTransitions){
				mainView.getGraph().insertTransition(t);
			}
			
			// get List of states to place in directSons & move them both graphically and in the directSosn set
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
			// removing composite states & initial one ! 
			Set<State> toBeRemoved = new HashSet<State>();
			for(State s : getAllStates()){	
				if(s.isCompositeState()){
					// graphically explode composite state
					for(State son : ((CompositeState)s).getStates()){
						if(son.isInitialState()){
							toBeRemoved.add(son);
						}
					}
					toBeRemoved.add(s);
				}	
			}
			
			for(State s : toBeRemoved){
				removeState(s);
			}
			refreshGraph();
			mainView.getGraph().informUser("Operation performed !");
		}
		else{
			mainView.getGraph().informUser("The graph must be valid in order to perform this operation");
		}
	}
	/*
	public void flatten2(){
		
		if(true){
		//if(validate()){
			InitialState init = null;
			for(State s : directSons){
				if(s.isInitialState()){
					init = (InitialState) s;
				}
			}
			init.apply(flattenVisitor);
			mainView.getGraph().informUser("Operation performed !");
		}else{
			mainView.getGraph().informUser("The graph must be valid in order to perform this operation");
		}
	}
	*/
	private boolean areSomeTransitionsLinkedToCompositeState() {
		for(Transition<State> t: getAllTransitions()){
			if (t.getDestination().isCompositeState() || t.getSource().isCompositeState()){
				return true;
			}
		}
		return false;
	}

	/*
	 * Retrieve all states
	 */
	public List<State> getAllStates(){
		List<State> states = new ArrayList<State>();
		for(State s : directSons){
			states.addAll(s.getAllStates());
		}
		return states;
	}
	
	public Set<Transition<State>> getAllTransitions(){
		Set<Transition<State>> transitions = new HashSet<Transition<State>>();
		for(State s : directSons){
			transitions.addAll(s.getAllTransitions());
		}
		return transitions;
	}

	public void updateStateName(NamedState state, String label, List<State> states) {
		states.remove(state);
		if (!verifyName(label, states)) {
			label = changeName(label);
		}
		state.setName(label);
		mxCell newCell = getLinkedStates().get(state);
		newCell.setValue(label);
		mainView.getGraph().getGraph().refresh();
	}

	public void updateTransitionName(Transition<State> transition, String label) {
		if (transition instanceof StandardTransition) {
			String[] parts = label.split("/");
			System.out.println(parts);
			label= parts[0];
			if (parts.length > 1)
				updateGuard((StandardTransition)transition, parts[1]);
		}
		
		transition.setAction(new Action(label));
		mxCell newCell = getLinkedTransitions().get(transition);
		if (transition instanceof StandardTransition)
			newCell.setValue(transition.getAction().getName() + " / " + ((StandardTransition)transition).getGuard().getCondition());
		else
			newCell.setValue(transition.getAction().getName());
		mainView.getGraph().getGraph().refresh();
		System.out.println("Action is : " + transition.getAction().getName());
	}
	
	public void updateGuard(StandardTransition transition, String label) {

		transition.getGuard().setCondition(label);
		mxCell newCell = getLinkedTransitions().get(transition);
		newCell.setValue(transition.getAction().getName() + " / " + transition.getGuard().getCondition());
		mainView.getGraph().getGraph().refresh();
		System.out.println("Guard is : " + transition.getGuard().getCondition());
	}
	
	public void  refreshGraph(){
		CustomMxGraph graph = mainView.getGraph().getGraph();
		// Save positions 
		Map<State, mxCell> tmpStates = getLinkedStates();
		Map<Transition<State>, mxCell> tmpTransitions = getLinkedTransitions();
		
		//getLinkedStates() = new HashMap<State, mxCell>();
		//linkedTransitions = new HashMap<Transition<State>, mxCell>();
		
		// Reset graph 
		graph.setReactToDeleteEvent(false);
		graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
		graph.setReactToDeleteEvent(true);
		
		for(State s : directSons){
			if(s.isInitialState()){
				mainView.getGraph().insertState((InitialState)s, null);
			}else if(s.isFinalState()){
				mainView.getGraph().insertState((FinalState)s, null);
			}
			else if(s.isCompositeState()){
				mainView.getGraph().insertState((CompositeState)s, null);
			}
			else {
				mainView.getGraph().insertState((SimpleState)s, null);
			}
			if(s.isCompositeState()){
				s.apply(drawerVisitor);
			}
		}
		
		
		for(Transition<State> t : getAllTransitions()){
			mainView.getGraph().insertTransition(t);
		}
		
	}

	public MainView getView() {
		return mainView;
	}

    public Set<State> getDirectSons() {
        return directSons;
    }
}
