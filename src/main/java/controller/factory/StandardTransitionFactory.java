package controller.factory;

import model.Action;
import model.Event;
import model.Guard;
import model.StandardTransition;
import model.State;

/**
 * 
 * @author ncouret
 *
 */
enum StandardTransitionFactory implements ITransitionFactory<StandardTransition, State> {

	INSTANCE {

		@Override
		public StandardTransition create(State source, State destination) {
			StandardTransition t = new StandardTransition(source, destination);
			return t;
		}

		@Override
		public StandardTransition create(State source, State destination, Action action) {
			StandardTransition t = create(source, destination);
			t.setAction(action);
			return t;
		}

		@Override
		public StandardTransition create(State source, State destination, Event event) {
			StandardTransition t = create(source, destination);
			t.setEvent(event);
			return t;
		}

		@Override
		public StandardTransition create(State source, State destination, Guard guard) {
			StandardTransition t = create(source, destination);
			t.setGuard(guard);
			return t;
		}

		@Override
		public StandardTransition create(State source, State destination, Action action, Event event) {
			StandardTransition t = create(source, destination, action);
			t.setEvent(event);
			return t;
		}

		@Override
		public StandardTransition create(State source, State destination, Action action, Guard guard) {
			StandardTransition t = create(source, destination, action);
			t.setGuard(guard);
			return t;
		}

		@Override
		public StandardTransition create(State source, State destination, Event event, Guard guard) {
			StandardTransition t = create(source, destination, event);
			t.setGuard(guard);
			return t;
		}

		@Override
		public StandardTransition create(State source, State destination, Action action, Event event,
				Guard guard) {
			StandardTransition t = create(source, destination, action, event);
			t.setGuard(guard);
			return t;
		}
		
	}
	
	
}
