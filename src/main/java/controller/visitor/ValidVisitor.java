package controller.visitor;

import controller.Diagram;
import model.CompositeState;
import model.FinalState;
import model.InitialState;
import model.SimpleState;
import model.error.DiagramError;

import java.util.Set;

public class ValidVisitor implements Visitor {

    /**
     * Always valid as a state
     *
     * @param s
     */
    @Override
    public void visit(SimpleState s) {
        Diagram.getInstance().getValidator().setValid(true);
    }

    /**
     * Composite state is valid if there is one and only one initial state An
     * initial state in a composite can't go out of its composite
     *
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
            if (init.getOutgoingTransitions().size() == 1) {
                Transition t =
                        (Transition) init.getOutgoingTransitions().toArray()[0];
                // si l'état pointé par l'état initial n'est pas dans les
                // fils de
                // son parent alors il pointe vers un etat hors de la boite
                // => erreur
                Set<State> sons = s.getStates();
                if (!sons.contains(t.getDestination())) {
                    Diagram.getInstance().getValidator()
                            .addError(new DiagramError(s.toString() +
                                                       " has its initial " +
                                                       "state link to another" +
                                                       " " +
                                                       "state outside of the " +
                                                       "composite " +
                                                       "state"));
                    Diagram.getInstance().getValidator().setValid(false);
                }
            }
        }
    }

    /**
     * Initial state is valid if there is one and only one transition outgoing
     * from it
     *
     * @param s
     */
    @Override
    public void visit(InitialState s) {
        int transitionsSize = s.getOutgoingTransitions().size();
        if (transitionsSize > 1) {
            Diagram.getInstance().getValidator().addError(new DiagramError(
                    "All initial states must have only one transition"));
            Diagram.getInstance().getValidator().setValid(false);
        } else if (transitionsSize == 0) {
            Diagram.getInstance().getValidator().addError(new DiagramError(
                    "All initial states must have one transition minimum"));
            Diagram.getInstance().getValidator().setValid(false);
        }
    }

    /**
     * No condition proper to final state only
     *
     * @param s
     */
    @Override
    public void visit(FinalState s) {
        Diagram.getInstance().getValidator().setValid(true);
    }
}
