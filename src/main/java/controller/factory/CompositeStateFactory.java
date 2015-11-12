/**
 * 
 */
package controller.factory;

import model.CompositeState;
import view.GraphView;

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
		graph.insertState(s);
		return s;
	}

}
