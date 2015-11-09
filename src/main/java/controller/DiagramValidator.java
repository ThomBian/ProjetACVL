package controller;
import controller.visitor.ValidVisitor;
import model.*;
import model.error.DiagramError;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Thomas on 05/11/15.
 */
public class DiagramValidator {
    private boolean            isValid;
    private List<DiagramError> errors;
    private State              validationInput;
    private ValidVisitor       validVisitor;

    public DiagramValidator() {
        errors = new ArrayList<>();
        validVisitor = new ValidVisitor();
        isValid = true;
    }

    public boolean validate(boolean displayErrors) {
        areStatesValid();
        areAllStatesReachable();
//        areCompositeValid();
        if (displayErrors)
            Diagram.getInstance().getView().displayValidationWindow(errors);
        errors.clear();
        return isValid;
    }

    private void areAllStatesReachable() {
        areNbInitialFinalStatesValid();
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
            s.setAlreadyTest(false);
        }
    }

    private void areNbInitialFinalStatesValid() {
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
    }
/**
    private void areCompositeValid() {
        for (State s : Diagram.getInstance().getAllStates()){
            if (s.isCompositeState()){
                InitialState init = ((CompositeState) s).getInitState();
                if(init != null){

                }
            }
        }
    }**/

    private void areStatesValid() {
        for (State s : Diagram.getInstance().getAllStates()) {
            s.apply(validVisitor);
        }
    }

    public void addError(DiagramError e) {
        errors.add(e);
    }

    public void setValid(boolean valid) {
        isValid = valid & isValid;
    }
}
