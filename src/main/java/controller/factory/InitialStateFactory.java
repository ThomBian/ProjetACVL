/**
 * 
 */
package controller.factory;

import com.mxgraph.model.mxCell;

import model.InitialState;
import view.GraphView;
import view.Style;

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
			mxCell cell = (mxCell) graph.getGraph().createVertex(graph.getGraph().getDefaultParent(), null, "", 20, 20,
					30, 30, Style.INITIAL);
			is.setGraphic(cell);
			graph.getGraph().addCell(cell);
			return is;
		}
	}
}
