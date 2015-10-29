package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public abstract class State {
	private Set<Transition<State>> outgoingTransitions;
	
	public Set<Transition<State>> getOutgoingTransitions() {
		return outgoingTransitions;
	}
	public boolean isCompositeState(){
		return false;
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
