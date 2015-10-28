package model;

import java.util.Set;

public abstract class NamedState extends State {

	private String name;
	private Set<StandardTransition> outgoingTransitions;
	

	public NamedState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Set<StandardTransition> getOutgoingTransitions() {
		return outgoingTransitions;
	}
}
