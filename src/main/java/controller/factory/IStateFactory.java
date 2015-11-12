package controller.factory;

import model.State;
import view.GraphView;

/**
 * 
 * @author ncouret
 *
 * @param <T> 
 */
interface IStateFactory<T extends State> {
	
	/**
	 * 
	 * @param graph
	 * @return
	 */
	public T create(GraphView graph);
}
