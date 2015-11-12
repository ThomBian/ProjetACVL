package model;

import controller.visitor.Visitor;

import java.util.HashSet;
import java.util.Set;

public class InitialState extends State {

    public String toString() {
        return "[Initial State]";
    }

    public boolean isInitialState() {
        return true;
    }

    @Override
    public Set<State> getSimpleFinalStateInSons() {
        Set<State> states = new HashSet<State>();
        return states;
    }

    @Override
    public void apply(Visitor v) {
        v.visit(this);
    }
}
