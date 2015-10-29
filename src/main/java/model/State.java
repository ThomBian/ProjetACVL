package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class State {
	private Set<Transition<State>> outgoingTransitions = new HashSet<Transition<State>>();
	
	// TODO This is  not the place for transitions because it allows final transition to have transitions !!!
	public Set<Transition<State>> getOutgoingTransitions() {
		return outgoingTransitions;
	}
	public boolean isCompositeState(){
		return false;
	}
	public boolean removeTransitionInSons(Transition t){
		return outgoingTransitions.remove(t);
	}
	public List<State> getAllStates() {
		List<State> sons = new ArrayList<State>();
		sons.add(this);
		return sons;
	}

	public boolean isInitialState() {
		return false;
	}
}
