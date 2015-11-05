package controller;
import model.InitialState;
import model.State;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Thomas on 05/11/15.
 */
public class DiagrammValidator {
    private List<DiagramError> errors;

    public DiagrammValidator() {
        errors = new ArrayList<>();
    }

    public boolean validate() {
        boolean isValid = true;
        isValid = areAllStatesReachable();
        Diagram.getInstance().getView().displayValidationWindow(errors);
        errors.clear();
        return isValid;
    }

    private boolean areAllStatesReachable() {
        boolean isValid = true;
        int nbInitialState = 0;
        InitialState input = null;
        for(State s: Diagram.getInstance().getDirectSons()){
            if (s instanceof InitialState) {
                nbInitialState++;
                input = (InitialState) s;
            }
        }
        if (nbInitialState == 0 || nbInitialState > 1){
            this.addError(new DiagramError("Erreur avec les états initiaux"));
            isValid = false;
        }
        if(input != null){
            input.reach();
        }
        for(State s : Diagram.getInstance().getAllStates()){
            if (!s.isReach()){
                this.addError(new DiagramError("State unreachable : "+s.toString()));
                isValid = false;
            }
        }
        for(State s: Diagram.getInstance().getAllStates()){
            s.setReach(false);
        }
        return isValid;
    }

    public void addError(DiagramError e) {
        errors.add(e);
    }
}
