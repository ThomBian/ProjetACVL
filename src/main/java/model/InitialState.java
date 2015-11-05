package model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import controller.visitor.Visitor;

public class InitialState extends State {
	

	public String toString(){
		return "[Initial State]";
	}
	public boolean isInitialState() {
		return true;
	}

	@Override
	public Collection<? extends State> getSimpleFinalStateInSons() {
		Set<State> states = new HashSet<State>();
		return states;
	}
	@Override
	public void apply(Visitor v) {
		v.visit(this);
	}
}
