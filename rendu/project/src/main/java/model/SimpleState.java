package model;

import controller.visitor.Visitor;

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

    public String toString() {
        return "[State : " + this.getName() + "]";
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
