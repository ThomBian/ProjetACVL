package controller.factory;

import com.mxgraph.model.mxCell;

import model.FinalState;
import view.GraphView;
import view.Style;

enum FinalStateFactory implements IStateFactory<FinalState> {

	INSTANCE {
		@Override
		public FinalState create(GraphView graph) {
			FinalState fs = new FinalState();
			mxCell vertex = (mxCell) graph.getGraph().createVertex(graph.getGraph().getDefaultParent(), null, "", 20,
					20, 30, 30, Style.FINAL);
			fs.setGraphic(vertex);
			graph.getGraph().addCell(vertex);
			return fs;
		}
	}

}
