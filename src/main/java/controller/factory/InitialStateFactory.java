/**
 * 
 */
package controller.factory;

import model.InitialState;
import view.GraphView;

/**
 * <p>
 * 
 * @author ncouret
 *
 */
enum InitialStateFactory implements IStateFactory<InitialState> {

	INSTANCE {

		@Override
		public InitialState create(GraphView graph) {
			InitialState is = new InitialState();
			graph.insertState(is);
			return is;
		}
	}
}
