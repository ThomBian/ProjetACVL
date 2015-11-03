package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class State {
    //boolean switches to true during validation step
    //must be false at initialisation
    protected boolean reach = false;

	private Set<Transition<State>> outgoingTransitions = new HashSet<Transition<State>>();
	
	// TODO This is  not the place for transitions because it allows final transition to have transitions !!!
	public Set<Transition<State>> getOutgoingTransitions() {
		return outgoingTransitions;
	}
	public boolean isCompositeState(){
		return false;
	}
	public boolean isFinalState(){
		return false;
	}
	public boolean removeTransitionInSons(Transition<State> t){
		return outgoingTransitions.remove(t);
	}
	/*
	 * Retrieve all states including itself and the children
	 */
	public List<State> getAllStates() {
		List<State> sons = new ArrayList<State>();
		sons.add(this);
		return sons;
	}

    //abstract boolean isValid();

	public boolean isInitialState() {
		return false;
	}
	
	public boolean isNamedState() {
		return false;
	}
	/*
	 * Get & remove transition that leads to the target state
     * Return the transitions to be removed
	 */
	public List<Transition<State>> removeTransitionInSonsFromTarget(State target) {
		List<Transition<State>> toBeRemoved = new ArrayList<Transition<State>>();
		for(Transition<State>  t : outgoingTransitions){
			if(t.getDestination().equals(target)){
				toBeRemoved.add(t);
			}
		}
		for(Transition<State> t : toBeRemoved){
			outgoingTransitions.remove(t);
		}
		return toBeRemoved;
	}

    public void reach() {
        this.reach = true;
        for(Transition<State> t: outgoingTransitions){
            State s = t.getDestination();
           	s.reach();
        }
    }

	public Collection<? extends Transition<State>> getAllTransitions() {
		return getOutgoingTransitions();
	}

	public abstract Collection<? extends State> getSimpleFinalStateInSons();

	public boolean isReach() {
		return reach;
	}

	public void setReach(boolean reach) {
		this.reach = reach;
	}
}
