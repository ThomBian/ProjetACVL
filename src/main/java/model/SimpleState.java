package model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
	boolean isValid() {
		return true;
	}

	@Override
	public Collection<? extends State> getSimpleFinalStateInSons()  {
		Set<State> states = new HashSet<State>();
		states.add(this);
		return states;
	}
}
