/**
 * 
 */
package controller.factory;

import com.mxgraph.model.mxCell;

import model.SimpleState;
import view.GraphView;
import view.Style;

/**
 * @author ncouret
 *
 */
class SimpleStateFactory implements IStateFactory<SimpleState> {
	
	private static volatile SimpleStateFactory instance;

	public static SimpleStateFactory getInstance() {
		if (instance == null) {
			synchronized (SimpleStateFactory.class) {
				if (instance == null) {
					instance = new SimpleStateFactory();
				}
			}
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see controller.factory.StateFactory#create(view.GraphView)
	 */
	@Override
	public SimpleState create(GraphView graph) {
		SimpleState s = create(graph, "");
		return s;
	}
	
	public SimpleState create(GraphView graph, String name) {
		SimpleState s = new SimpleState(name);
		mxCell vertex = (mxCell) graph.getGraph().createVertex(graph.getGraph().getDefaultParent(), null, name, 20, 20, 80, 30, Style.STATE);
		graph.insertState(s);
		s.setGraphic(vertex);
		return s;
	}

}
