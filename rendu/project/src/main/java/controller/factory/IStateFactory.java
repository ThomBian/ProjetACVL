package controller.factory;

import model.State;
import view.GraphView;

/**
 * @param <T>
 *
 * @author ncouret
 */
interface IStateFactory<T extends State> {

    /**
     * @param graph
     *
     * @return
     */
    public T create(GraphView graph);
}
