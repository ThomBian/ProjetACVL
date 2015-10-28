package model;

import java.util.HashSet;
import java.util.Set;

public final class CompositeState extends NamedState {
	
	private InitialState initState;
	private Set<State> states;
	private InitialTransition initTrans;
	private Set<Transition<State>> transitions;

	public CompositeState(String name) {
		super(name);
		states = new HashSet<State>();
		transitions = new HashSet<Transition<State>>();
	}

	public InitialState getInitState() {
		return initState;
	}

	public void setInitState(InitialState initState) {
		this.initState = initState;
	}

	public Set<State> getStates() {
		return states;
	}

	public void setStates(Set<State> states) {
		this.states = states;
	}

	public InitialTransition getInitTrans() {
		return initTrans;
	}

	public void setInitTrans(InitialTransition initTrans) {
		this.initTrans = initTrans;
	}

	public Set<Transition<State>> getTransitions() {
		return transitions;
	}

	public void setTransitions(Set<Transition<State>> transitions) {
		this.transitions = transitions;
	}
	
	@Override
	public boolean isCompositeState(){
		return true;
	}
	
	public CompositeState findParentState(State s){
    	CompositeState result = null;
		for(State cur : states){
    		if(cur.equals(s)) return this;
    		if(cur.isCompositeState()){
    			result = ((CompositeState)cur).findParentState(s);
    			if(result != null) return result;
    		}
    	}
		return result;
    }
	
	public String toString(){
		String result = new String();
		result +="CompositeState : " + this.getName()+"\n";
		
		for(State s : states){
			result += "\t"+s.toString() + "\n";
		}
		result +="CompositeState END\n" ;
		return result;
	}
}
