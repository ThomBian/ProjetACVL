package controller.visitor;

import controller.Diagram;
import model.*;

import java.util.HashSet;
import java.util.Set;

public class FlattenVisitor implements Visitor {

    @Override
    public void visit(SimpleState s) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void visit(CompositeState c) {
        Diagram d = Diagram.getInstance();
        Set<Transition<State>> transitionToRemove = new HashSet<Transition<State>>();
        Set<State> stateToRemove = new HashSet<State>();
        Set<State> stateToAdd = new HashSet<State>();
        State newDest = null;
        Set<State> states = null;

        for (State s : c.getStates()) {
            s.apply(this);
            if (s.isCompositeState()) {
                stateToAdd.addAll(((CompositeState) s).getSimpleFinalStateInSons());
                stateToRemove.add(s);
            }
        }
        c.getStates().addAll(stateToAdd);
        // Handle out going transitions
        for (Transition<State> t : c.getOutgoingTransitions()) {
            newDest = t.getDestination();
            while (newDest.isCompositeState()) {
                newDest = (State) ((CompositeState) newDest).getInitState().getOutgoingTransitions().toArray()[0];
            }
            states = c.getSimpleFinalStateInSons();
            for (State s : states) {
                if (!s.isFinalState()) {
                    d.addTransition(s, newDest);
                }
            }
            transitionToRemove.add(t);
        }
        // Handle incoming transitions
        for (Transition<State> t : c.getIncomingTransitions()) {
            newDest = t.getDestination();

            while (newDest.isCompositeState()) {
                Transition<State> transition =
                        (Transition<State>) ((CompositeState) newDest).getInitState().getOutgoingTransitions()
                                .toArray()[0];
                newDest = transition.getDestination();
            }
            // TODO transform initial transition into standard transition if needed

            c.getIncomingTransitions().remove(t);
            t.setDestination(newDest);
        }
        // Remove everything
        d.removeTransitions(transitionToRemove);
        for (State s : stateToRemove) {
            d.removeState(s, false);
        }
    }

    @Override
    public void visit(InitialState s) {

    }

    @Override
    public void visit(FinalState s) {

    }
}
