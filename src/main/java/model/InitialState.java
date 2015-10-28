package model;

import java.util.Set;

public class InitialState extends State {
	private Set<Transition<State>> outgoingTransitions;
	
	public Set<Transition<State>> getOutgoingTransitions() {
		return outgoingTransitions;
	}

	public String toString(){
		return "initialState";
	}
}
