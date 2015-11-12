package controller.visitor;

import controller.Diagram;
import model.*;
import view.GraphView;

public class StateDrawerVisitor implements Visitor {

    @Override
    public void visit(SimpleState s) {

    }

    @Override
    public void visit(CompositeState c) {
        GraphView graph = Diagram.getInstance().getView().getGraph();
        for (State s : c.getStates()) {
            if (s.isInitialState()) {
                graph.insertState((InitialState) s, null);
            } else if (s.isFinalState()) {
                graph.insertState((FinalState) s, null);
            } else if (s.isCompositeState()) {
                graph.insertState((CompositeState) s, null);
            } else {
                graph.insertState((SimpleState) s, null);
            }
            if (s.isCompositeState()) {
                s.apply(this);
            }
        }
    }

    @Override
    public void visit(InitialState s) {

    }

    @Override
    public void visit(FinalState s) {

    }
}
