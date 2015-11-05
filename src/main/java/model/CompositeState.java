package model;

import java.util.*;

public final class CompositeState extends NamedState {

    private Set<State> states;

    public CompositeState(String name) {
        super(name);
        states = new HashSet<State>();
    }

    /*
     * Return the initial state of a composite one
     * Precondition The composite state must have only one initial state !!!
     */
    public InitialState getInitState() {
        int nbInitState = 0;
        InitialState init = null;
        for (State s : states) {
            if (s.isInitialState()) {
                nbInitState++;
                init = (InitialState) s;
            }
        }
        if (nbInitState == 0 || nbInitState > 1) {
            return null;
        }
        return init;
    }

    public Set<State> getStates() {
        return states;
    }

    public void setStates(Set<State> states) {
        this.states = states;
    }

    @Override
    public boolean isCompositeState() {
        return true;
    }

    public CompositeState findParentState(State s) {
        CompositeState result = null;
        for (State cur : states) {
            if (cur.equals(s))
                return this;
            if (cur.isCompositeState()) {
                result = ((CompositeState) cur).findParentState(s);
                if (result != null)
                    return result;
            }
        }
        return result;
    }

    public String toString() {
        String result = new String();
        result += "[CompositeState : " + this.getName() + "\n";
        if (!states.isEmpty()) {
            for (State s : states) {
                result += "\t" + s.toString() + "\n";
            }
        }
        result += "CompositeState END]\n";
        return result;
    }

    public List<State> getAllStates() {
        List<State> sons = new ArrayList<State>();
        sons.add(this);
        for (State s : states) {
            sons.addAll(s.getAllStates());
        }
        return sons;
    }

    public boolean removeTransitionInSons(Transition<State> t) {
        if (getOutgoingTransitions().remove(t))
            return true;
        for (State s : states) {
            if (s.removeTransitionInSons(t))
                return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see model.State#removeTransitionInSonsFromTarget(model.State)
     * Get & remove transition that leads to the target state
     * Return the transitions to be removed
     */
    public List<Transition<State>> removeTransitionInSonsFromTarget(
            State target) {
        List<Transition<State>> toBeRemoved =
                new ArrayList<Transition<State>>();
        for (Transition<State> t : getOutgoingTransitions()) {
            if (t.getDestination().equals(target)) {
                toBeRemoved.add(t);
            }
        }
        for (Transition<State> t : toBeRemoved) {
            getOutgoingTransitions().remove(t);
        }
        for (State s : states) {
            toBeRemoved.addAll(s.removeTransitionInSonsFromTarget(target));
        }
        return toBeRemoved;
    }

    @Override
    public Collection<? extends Transition<State>> getAllTransitions() {
        Set<Transition<State>> transitions = new HashSet<Transition<State>>();
        transitions.addAll(getOutgoingTransitions());
        for (State s : states) {
            transitions.addAll(s.getAllTransitions());
        }
        return transitions;
    }

    @Override
    public Collection<? extends State> getSimpleFinalStateInSons() {
        Set<State> finalSimple = new HashSet<State>();
        for (State s : states) {
            finalSimple.addAll(s.getSimpleFinalStateInSons());
        }
        return finalSimple;
    }

    @Override
    public void reach() {
        this.reach = true;
        InitialState initState = getInitState();
        if (initState != null) {
            initState.reach();
            for (Transition<State> t : getOutgoingTransitions()) {
                State s = t.getDestination();
                s.reach();
            }
        }
    }

    @Override
    boolean isValid() {
        boolean isValid = true;
        InitialState s = getInitState();
        if(s == null){
            isValid = false;
        }
        return isValid;

    }
}
