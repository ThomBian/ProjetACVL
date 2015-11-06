package model;

import java.util.HashSet;
import java.util.Set;

import controller.visitor.Visitor;
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
	public Set<State> getSimpleFinalStateInSons() {
		Set<State> states = new HashSet<State>();
		states.add(this);
		return states;
	}
	@Override
	public void apply(Visitor v) {
		v.visit(this);
	}
}
