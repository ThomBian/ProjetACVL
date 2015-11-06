package controller.visitor;

import controller.Diagram;
import model.CompositeState;
import model.FinalState;
import model.InitialState;
import model.SimpleState;
import model.error.DiagramError;

public class ValidVisitor implements Visitor {

    /**
     * Always valid as a state
     * @param s
     */
    @Override
    public void visit(SimpleState s) {
        Diagram.getInstance().getValidator().setValid(true);
    }

    /**
     * Composite state is valid if there is one and only one initial state
     * @param s
     */
    @Override
    public void visit(CompositeState s) {
        if(s.getInitState() == null){
            Diagram.getInstance().getValidator().addError(new DiagramError(
                    s.toString() + "must contain one and only one initial " +
                    "state"));
            Diagram.getInstance().getValidator().setValid(false);
        } else {
            Diagram.getInstance().getValidator().setValid(true);
        }
    }

    /**
     * Initial state is valid if there is one and only one transition
     * outgoing from it
     * @param s
     */
    @Override
    public void visit(InitialState s) {
        int transitionsSize = s.getOutgoingTransitions().size();
        if (transitionsSize > 1) {
            Diagram.getInstance().getValidator().addError(new DiagramError(
                    "All initial states must have only one transition"));
            Diagram.getInstance().getValidator().setValid(false);
        } else if (transitionsSize == 0){
            Diagram.getInstance().getValidator().addError(new DiagramError(
                    "All initial states must have one transition minimum"));
            Diagram.getInstance().getValidator().setValid(false);
        }
    }

    /**
     * No condition proper to final state only
     * @param s
     */
    @Override
    public void visit(FinalState s) {
        Diagram.getInstance().getValidator().setValid(true);
    }
}
