package controller.factory;

import model.Action;
import model.Event;
import model.Guard;
import model.InitialState;
import model.InitialTransition;
import model.StandardTransition;
import model.State;

/**
 * 
 * @author ncouret
 *
 */
public final class TransitionFactory {
	
	private TransitionFactory() {
	}
	
	public static InitialTransition createInitialTransition(InitialState source, State destination) {
		return InitialTransitionFactory.INSTANCE.create(source, destination);
	}
	
	public static InitialTransition createInitialTransition(InitialState source, State destination, Action action) {
		return InitialTransitionFactory.INSTANCE.create(source, destination, action);
	}
	
	public static StandardTransition createStandardTransition(State source, State destination) {
		return StandardTransitionFactory.INSTANCE.create(source, destination);
	}
	
	public static StandardTransition createStandardTransition(State source, State destination, Event event) {
		return StandardTransitionFactory.INSTANCE.create(source, destination, event);
	}
	
	public static StandardTransition createStandardTransition(State source, State destination, Guard guard) {
		return StandardTransitionFactory.INSTANCE.create(source, destination, guard);
	}
	
	public static StandardTransition createStandardTransition(State source, State destination, Action action, Event event) {
		return StandardTransitionFactory.INSTANCE.create(source, destination, action, event);
	}
	
	public static StandardTransition createStandardTransition(State source, State destination, Action action, Guard guard) {
		return StandardTransitionFactory.INSTANCE.create(source, destination, action, guard);
	}
	
	public static StandardTransition createStandardTransition(State source, State destination, Event event, Guard guard) {
		return StandardTransitionFactory.INSTANCE.create(source, destination, event, guard);
	}
	
	public static StandardTransition createStandardTransition(State source, State destination, Action action, Event event, Guard guard) {
		return StandardTransitionFactory.INSTANCE.create(source, destination, action, event, guard);
	}
}

