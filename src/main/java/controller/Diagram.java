package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.UIManager;

import com.mxgraph.model.mxCell;

import controller.factory.StateFactory;
import controller.factory.TransitionFactory;
import controller.visitor.FlattenVisitor;
import controller.visitor.StateDrawerVisitor;
import model.Action;
import model.CompositeState;
import model.Event;
import model.FinalState;
import model.Guard;
import model.InitialState;
import model.NamedState;
import model.SimpleState;
import model.StandardTransition;
import model.State;
import model.Transition;
import view.CustomMxGraph;
import view.MainView;

public class Diagram {

	private static final Diagram instance = new Diagram();

	private DiagramValidator validator;

	public static Diagram getInstance() {
		return instance;
	}

	private MainView mainView;
	private FlattenVisitor flattenVisitor = new FlattenVisitor();
	private StateDrawerVisitor drawerVisitor = new StateDrawerVisitor();

	private Set<State> directSons = new HashSet<State>();

	private Diagram() {

	}

	public void createInitialState() {
		InitialState s = StateFactory.createInitialState(mainView.getGraph());
		directSons.add(s);
	}

	public void createState(String name) {
		if (!verifyName(name, getAllStates())) {
			name = changeName(name);
		}
		SimpleState s = StateFactory.createSimpleState(mainView.getGraph(), name);
		directSons.add(s);
	}

	public void createCompositeState(String name) {
		if (!verifyName(name, getAllStates())) {
			name = changeName(name);
		}
		CompositeState s = StateFactory.createCompositeState(mainView.getGraph(), name);
		directSons.add(s);
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
			s.setInCompositeState(true);
		}else if(parent != null && c == null){
			parent.getStates().remove(s);
			// add new link
			directSons.add(s);
			s.setInCompositeState(false);
		}else if (parent == null && c != null){
			directSons.remove(s);
			// add new link
			c.getStates().add(s);
			s.setInCompositeState(true);
		}
	}

	public boolean validate(boolean display) {
		validator = new DiagramValidator();
		return validator.validate(display);
	}

	private void removeTransitionsFromState(State s){
		for(Transition<State> t : s.getIncomingTransitions()){
			t.getSource().getOutgoingTransitions().remove(t);
			getLinkedTransitions().remove(t);
		}
		for(Transition<State> t : s.getOutgoingTransitions()){
			t.getDestination().getIncomingTransitions().remove(t);
			getLinkedTransitions().remove(t);
		}
		// TODO remove the transitions below from linkedtran
		s.getIncomingTransitions().clear();
		s.getOutgoingTransitions().clear();
	}

    private Map<State, mxCell> getLinkedStates(){
    	return mainView.getGraph().getLinkedStates();
    }
    private Map<Transition<State>, mxCell> getLinkedTransitions(){
    	return mainView.getGraph().getLinkedTransitions();
    }
	/*
	 * Remove a state and the transitions leading to it
	 * If its a composite one, the sons are deleted and the transitions too
	 * removeoldPosition inform if we want to remerber the position of the state for later user
	 */
	public void removeState(State s, boolean removeOldPosition) {
		// remove transitions linked to this state
		List<State> sonsAndFather = s.getAllStates();
		for(State son : sonsAndFather){
			removeTransitionsFromState(son);
			if(removeOldPosition)
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
			// Linux UI
			try{
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			}catch(Exception e){
				
			}
			mainView = new MainView();
			mainView.getFrame().setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createFinalState() {
		FinalState s = StateFactory.createFinalState(mainView.getGraph());
		directSons.add(s);
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

	@SuppressWarnings("unchecked")
	public void addTransitionAndRefreshView(State sourceState, State targetState, mxCell transition) {
		Transition<?> t = addTransitionToModel(sourceState, targetState);
		getLinkedTransitions().put((Transition<State>) t, transition);
		updateTransitionName((Transition<State>) t,"Default transition");
	}
	@SuppressWarnings("unchecked")
	private Transition<?> addTransitionToModel(State sourceState, State targetState){
		Transition<? extends State> t;
		if(sourceState.isInitialState()){
			t = TransitionFactory.createInitialTransition((InitialState) sourceState, targetState, null);
		}else{
			t = TransitionFactory.createStandardTransition(sourceState, targetState, null, new Guard("Default Guard"));
		}
		sourceState.getOutgoingTransitions().add((Transition<State>) t);
		targetState.getIncomingTransitions().add((Transition<State>) t);
		return t;
	}
	
	public void addTransition(State sourceState, State targetState) {
		addTransitionToModel(sourceState, targetState);
	}
	
	/*
	 * Remove this transition from everywhere (incl. states)
	 */
	public void removeTransition(Transition<State> transition){
		getLinkedTransitions().remove(transition);
		transition.destroy();
	}
	
	public void removeTransitions(Set<Transition<State>> transitions){
		for(Transition<State> t : transitions){
			removeTransition(t);
		}
	}

	/*
	 * Flatten a valid graph
	 */
	public void flatten(){
		if(validate(false)){
			// save graphical components if case we want to draw the with the same position as before 
			Set<CompositeState> statesToDelete = new HashSet<CompositeState>();
			Set<State> statesToAdd= new HashSet<State>();
			for(State s : directSons){
				s.apply(flattenVisitor);
				if(s.isCompositeState()){
					statesToAdd.addAll(((CompositeState) s).getSimpleFinalStateInSons());
					statesToDelete.add((CompositeState) s);
				}
			}
			directSons.removeAll(statesToDelete);
			directSons.addAll(statesToAdd);
			refreshGraph();
			mainView.getGraph().informUser("Operation performed !");
		}else{
			mainView.getGraph().informUser("The graph must be valid in order to perform this operation");
		}
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
	
	private Set<Transition<State>> getAllTransitions(){
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
		mainView.getGraph().updateStateLabel(state);
	}

	/*
	 * Formats and update Transition's name
	 */
	public void updateTransitionName(Transition<State> transition, String label) {
		if (transition.isStandardTransition()) {
			String[] parts = label.split(" / ");
			label= parts[0];
			if (parts.length == 1) {
				((StandardTransition)transition).setEvent(null);
				((StandardTransition)transition).setGuard(null);
			}
			if (parts.length == 2) {
				if (parts[1].startsWith("[")) {
					updateGuard((StandardTransition)transition, parts[1]);
					((StandardTransition)transition).setEvent(null);
				}
				else {
					updateEvent((StandardTransition)transition, parts[1]);
					((StandardTransition)transition).setGuard(null);
				} 
			}
			if (parts.length >= 3) {
				updateEvent((StandardTransition)transition, parts[1]);
				updateGuard((StandardTransition)transition, parts[2]);
			}			
		}
		
		transition.setAction(new Action(label));
		mainView.getGraph().updateTransitionLabel(transition);
	}
	

	/*
	 * Formats and update Event
	 */
	private void updateEvent(StandardTransition transition, String label) {
		System.out.println("updateEvent de " + label);
		if (!(label.isEmpty())){
			String newstr = "(";
			if(!(label.startsWith("(")))
				label = newstr.concat(label);
			System.out.println("etat label : " + label);
			if(!(label.endsWith(")")))
				label = label.concat(")");
			System.out.println("etat label : " + label);
			if (transition.getEvent() != null)
				transition.getEvent().setName(label);
			else {
				transition.setEvent(new Event(label));
			}
			System.out.println("etat label : " + label);
			System.out.println("transition.getEvent.getName : " + transition.getEvent().getName());
			mainView.getGraph().updateTransitionLabel(transition);
		} else {
			transition.setEvent(null);
		}
	}

	/*
	 * Formats and update Guard
	 */
	private void updateGuard(StandardTransition transition, String label) {
		if (!(label.isEmpty())){
			String newstr = "[";
			if(!(label.startsWith("[")))
				label = newstr.concat(label);
			if(!(label.endsWith("]")))
				label = label.concat("]");
			if (transition.getGuard() != null)
				transition.getGuard().setCondition(label);
			else {
				transition.setGuard(new Guard(label));
			}
			mainView.getGraph().updateTransitionLabel(transition);
		} else {
			transition.setGuard(null);
		}
	}
	
	/*
	 * Refresh the graph from data model (erase all existing graphical components)
	 */
	public void  refreshGraph(){
		CustomMxGraph graph = mainView.getGraph().getGraph();
		// Save positions 
		getView().getGraph().setTmpStates(getLinkedStates());
		getView().getGraph().setLinkedStates(new HashMap<State, mxCell>());
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
				s.apply(drawerVisitor);
			}
			else {
				mainView.getGraph().insertState((SimpleState)s, null);
			}
		}
		for(Transition<State> t : getAllTransitions()){
			mainView.getGraph().insertTransition(t);
		}
		getView().getGraph().setTmpStates(null);
	}

	public MainView getView() {
		return mainView;
	}

	public Set<State> getDirectSons() {
		return directSons;
	}
	public DiagramValidator getValidator() {
		return validator;
	}
}
