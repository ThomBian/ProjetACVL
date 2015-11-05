package controller;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * Created by Thomas on 05/11/15.
 */
public class DiagrammValidator {
    private List<DiagramError> errors;
    private State              validationInput;

    public DiagrammValidator() {
        errors = new ArrayList<>();
    }

    public boolean validate() {
        boolean isValid = areAllStatesReachable();
        isValid = isValid & areCompositeValid();
        Diagram.getInstance().getView().displayValidationWindow(errors);
        errors.clear();
        return isValid;
    }

    private boolean areAllStatesReachable() {
        boolean isValid = areInitialFinalStatesValid();
        if (validationInput != null) {
            validationInput.reach();
        }
        for (State s : Diagram.getInstance().getAllStates()) {
            if (!s.isReach()) {
                this.addError(new DiagramError(
                        "State unreachable : " + s.toString()));
                isValid = false;
            }
        }
        for (State s : Diagram.getInstance().getAllStates()) {
            s.setReach(false);
        }
        return isValid;
    }

    private boolean areInitialFinalStatesValid() {
        boolean isValid = true;
        int nbInitialState = 0;
        int nbFinalState = 0;
        for (State s : Diagram.getInstance().getDirectSons()) {
            if (s instanceof InitialState) {
                nbInitialState++;
                validationInput = s;
            } else if (s instanceof FinalState) {
                nbFinalState++;
            }
        }
        if (nbInitialState == 0 || nbInitialState > 1) {
            this.addError(new DiagramError("Diagram must contain one and " +
                                           "only one initial state..."));
            isValid = false;
        }
        if (nbFinalState > 1) {
            this.addError(new DiagramError("Diagram should contain one and " +
                                           "only one final state..."));
            isValid = false;
        }
        return isValid;
    }

    private boolean areCompositeValid() {
        boolean isValid = true;
        Set<Transition<State>> transitions =
                Diagram.getInstance().getAllTransitions();
        for (Transition t : transitions) {
            if (t.getDestination() instanceof CompositeState) {
                if (((CompositeState) t.getDestination()).getInitState() ==
                    null) {
                    this.addError(new DiagramError("Initial state(s) is(are) " +
                                                   "invalid in " +
                                                   t.getDestination()
                                                           .toString()));
                    isValid = false;
                }
            }
        }
        return isValid;
    }

    public void addError(DiagramError e) {
        errors.add(e);
    }
}
