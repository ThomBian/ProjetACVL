package model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
public class FinalState extends State {

	public String toString(){
		return "[Final State]";
	}

    @Override
    public void reach() {
        this.reach = true;
    }
    @Override
    public boolean isFinalState(){
		return true;
	}

	@Override
	public Collection<? extends State> getSimpleFinalStateInSons() {
		Set<State> states = new HashSet<State>();
		states.add(this);
		return states;
	}

}
