/**
 * 
 */
package controller.factory;

import model.Action;
import model.Event;
import model.Guard;
import model.InitialState;
import model.InitialTransition;
import model.State;

/**
 * <p>
 * {@link ITransitionFactory} implementation for {@link InitialTransition}.
 * InitialTransition cannot feature event or guard and thus these parameters are
 * ignored if the corresponding methods are called.
 * 
 * @author ncouret
 *
 */
enum InitialTransitionFactory implements ITransitionFactory<InitialTransition, InitialState> {

	INSTANCE {

		@Override
		public InitialTransition create(InitialState source, State destination) {
			InitialTransition t = new InitialTransition(source, destination);
			return t;
		}

		@Override
		public InitialTransition create(InitialState source, State destination, Action action) {
			InitialTransition t = create(source, destination);
			t.setAction(action);
			return t;
		}

		@Override
		public InitialTransition create(InitialState source, State destination, Event event) {
			return create(source, destination);
		}

		@Override
		public InitialTransition create(InitialState source, State destination, Guard guard) {

			return create(source, destination);
		}

		@Override
		public InitialTransition create(InitialState source, State destination, Action action,
				Event event) {

			return create(source, destination, action);
		}

		@Override
		public InitialTransition create(InitialState source, State destination, Action action,
				Guard guard) {

			return create(source, destination, action);
		}

		@Override
		public InitialTransition create(InitialState source, State destination, Event event,
				Guard guard) {

			return create(source, destination);
		}

		@Override
		public InitialTransition create(InitialState source, State destination, Action action,
				Event event, Guard guard) {

			return create(source, destination, action);
		}

	}

}
