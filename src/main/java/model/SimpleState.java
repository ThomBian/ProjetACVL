package model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import controller.visitor.Visitor;

/**
 * 
 *
 */
public final class SimpleState extends NamedState {

	public SimpleState(String name) {
		super(name);
	}

	public String toString(){
		return "[State : " + this.getName()+"]";
	}

	@Override
	public Collection<? extends State> getSimpleFinalStateInSons()  {
		Set<State> states = new HashSet<State>();
		states.add(this);
		return states;
	}
	@Override
	public void apply(Visitor v) {
		v.visit(this);
	}
}
