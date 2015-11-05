package controller.factory;

import view.GraphView;

/**
 * 
 * @author ncouret
 *
 * @param <T>
 */
public interface IFactory<T> {
	public T create(GraphView graph);
}
