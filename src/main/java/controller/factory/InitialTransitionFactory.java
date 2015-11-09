/**
 * 
 */
package controller.factory;

import com.mxgraph.model.mxCell;

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
		public InitialTransition create(InitialState source, State destination, mxCell graphic) {
			InitialTransition t = new InitialTransition(source, destination);
			t.setGraphic(graphic);
			return t;
		}

		@Override
		public InitialTransition create(InitialState source, State destination, mxCell graphic, Action action) {
			InitialTransition t = create(source, destination, graphic);
			t.setAction(action);
			return t;
		}

		@Override
		public InitialTransition create(InitialState source, State destination, mxCell graphic, Event event) {
			return create(source, destination, graphic);
		}

		@Override
		public InitialTransition create(InitialState source, State destination, mxCell graphic, Guard guard) {

			return create(source, destination, graphic);
		}

		@Override
		public InitialTransition create(InitialState source, State destination, mxCell graphic, Action action,
				Event event) {

			return create(source, destination, graphic, action);
		}

		@Override
		public InitialTransition create(InitialState source, State destination, mxCell graphic, Action action,
				Guard guard) {

			return create(source, destination, graphic, action);
		}

		@Override
		public InitialTransition create(InitialState source, State destination, mxCell graphic, Event event,
				Guard guard) {

			return create(source, destination, graphic);
		}

		@Override
		public InitialTransition create(InitialState source, State destination, mxCell graphic, Action action,
				Event event, Guard guard) {

			return create(source, destination, graphic, action);
		}

	}

}
