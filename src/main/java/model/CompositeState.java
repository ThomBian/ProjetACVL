package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CompositeState extends NamedState {

    private InitialState           initState;
    private Set<State>             states;

    public CompositeState(String name) {
        super(name);
        states = new HashSet<State>();
    }

    public InitialState getInitState() {
        return initState;
    }

    public void setInitState(InitialState initState) {
        this.initState = initState;
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
        for (State s : states) {
            sons.addAll(s.getAllStates());
        }
        return sons;
    }

    public boolean removeTransitionInSons(Transition t) {
        if (getOutgoingTransitions().remove(t))
            return true;
        for (State s : states) {
            if (getOutgoingTransitions().remove(t))
                return true;
        }
        return false;
    }

    public List<Transition> removeTransitionInSonsFromTarget(State target) {
        List<Transition> toBeRemoved = new ArrayList<Transition>();
        for (Transition t : getOutgoingTransitions()) {
            if (t.getDestination().equals(target)) {
                toBeRemoved.add(t);
            }
        }
        for (Transition t : toBeRemoved) {
            getOutgoingTransitions().remove(t);
        }
        for (State s : states) {
            toBeRemoved.addAll(s.removeTransitionInSonsFromTarget(target));
        }
        return toBeRemoved;
    }

    @Override
    public void setReach(boolean reach) {
        this.reach = reach;
        initState.setReach(true);
        for (Transition<State> t : getOutgoingTransitions()){
            State s = t.getDestination();
            s.setReach(true);
        }
    }
}
