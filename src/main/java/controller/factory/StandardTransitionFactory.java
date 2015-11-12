package controller.factory;

import com.mxgraph.model.mxCell;

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
		public StandardTransition create(State source, State destination, mxCell graphic) {
			StandardTransition t = new StandardTransition(source, destination);
			t.setGraphic(graphic);
			return t;
		}

		@Override
		public StandardTransition create(State source, State destination, mxCell graphic, Action action) {
			StandardTransition t = create(source, destination, graphic);
			t.setAction(action);
			return t;
		}

		@Override
		public StandardTransition create(State source, State destination, mxCell graphic, Event event) {
			StandardTransition t = create(source, destination, graphic);
			t.setEvent(event);
			return t;
		}

		@Override
		public StandardTransition create(State source, State destination, mxCell graphic, Guard guard) {
			StandardTransition t = create(source, destination, graphic);
			t.setGuard(guard);
			return t;
		}

		@Override
		public StandardTransition create(State source, State destination, mxCell graphic, Action action, Event event) {
			StandardTransition t = create(source, destination, graphic, action);
			t.setEvent(event);
			return t;
		}

		@Override
		public StandardTransition create(State source, State destination, mxCell graphic, Action action, Guard guard) {
			StandardTransition t = create(source, destination, graphic, action);
			t.setGuard(guard);
			return t;
		}

		@Override
		public StandardTransition create(State source, State destination, mxCell graphic, Event event, Guard guard) {
			StandardTransition t = create(source, destination, graphic, event);
			t.setGuard(guard);
			return t;
		}

		@Override
		public StandardTransition create(State source, State destination, mxCell graphic, Action action, Event event,
				Guard guard) {
			StandardTransition t = create(source, destination, graphic, action, event);
			t.setGuard(guard);
			return t;
		}
		
	}
	
	
}
