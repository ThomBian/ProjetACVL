package controller.factory;

import com.mxgraph.model.mxCell;

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
	
	public static InitialTransition createInitialTransition(InitialState source, State destination, mxCell graphic) {
		return InitialTransitionFactory.INSTANCE.create(source, destination, graphic);
	}
	
	public static InitialTransition createInitialTransition(InitialState source, State destination, mxCell graphic, Action action) {
		return InitialTransitionFactory.INSTANCE.create(source, destination, graphic, action);
	}
	
	public static StandardTransition createStandardTransition(State source, State destination, mxCell graphic) {
		return StandardTransitionFactory.INSTANCE.create(source, destination, graphic);
	}
	
	public static StandardTransition createStandardTransition(State source, State destination, mxCell graphic, Event event) {
		return StandardTransitionFactory.INSTANCE.create(source, destination, graphic, event);
	}
	
	public static StandardTransition createStandardTransition(State source, State destination, mxCell graphic, Guard guard) {
		return StandardTransitionFactory.INSTANCE.create(source, destination, graphic, guard);
	}
	
	public static StandardTransition createStandardTransition(State source, State destination, mxCell graphic, Action action, Event event) {
		return StandardTransitionFactory.INSTANCE.create(source, destination, graphic, action, event);
	}
	
	public static StandardTransition createStandardTransition(State source, State destination, mxCell graphic, Action action, Guard guard) {
		return StandardTransitionFactory.INSTANCE.create(source, destination, graphic, action, guard);
	}
	
	public static StandardTransition createStandardTransition(State source, State destination, mxCell graphic, Event event, Guard guard) {
		return StandardTransitionFactory.INSTANCE.create(source, destination, graphic, event, guard);
	}
	
	public static StandardTransition createStandardTransition(State source, State destination, mxCell graphic, Action action, Event event, Guard guard) {
		return StandardTransitionFactory.INSTANCE.create(source, destination, graphic, action, event, guard);
	}
}

