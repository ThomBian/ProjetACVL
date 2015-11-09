/**
 * 
 */
package controller.factory;

import com.mxgraph.model.mxCell;

import model.CompositeState;
import view.GraphView;
import view.Style;

/**
 * @author ncouret
 *
 */
final class CompositeStateFactory implements IStateFactory<CompositeState> {
	
	private static volatile CompositeStateFactory instance;
	
	public static CompositeStateFactory getInstance() {
		if(instance == null) {
			synchronized (CompositeStateFactory.class) {
				if(instance == null) {
					instance = new CompositeStateFactory();
				}
			}
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see controller.factory.StateFactory#create(view.GraphView)
	 */
	@Override
	public CompositeState create(GraphView graph) {
		return create(graph,"");
	}
	
	public CompositeState create(GraphView graph, String name) {
		CompositeState s = new CompositeState(name);
		mxCell vertex = (mxCell) graph.getGraph().createVertex(graph.getGraph().getDefaultParent(), null, name, 20, 20, 150, 180,
				Style.COMPOSITE);
		graph.getGraph().addCell(vertex);
		s.setGraphic(vertex);
		return s;
	}

}
