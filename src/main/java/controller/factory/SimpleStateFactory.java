/**
 * 
 */
package controller.factory;

import model.SimpleState;
import view.GraphView;

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
		graph.insertState(s);
		return s;
	}

}
