/**
 *
 */
package controller.factory;

import model.*;

/**
 * @author ncouret
 */
interface ITransitionFactory<T extends Transition<S>, S extends State> {

    /**
     * @param source
     * @param destination
     * @param graphic
     *
     * @return
     */
    public T create(S source, State destination);

    /**
     * @param source
     * @param destination
     * @param graphic
     * @param action
     *
     * @return
     */
    public T create(S source, State destination, Action action);

    /**
     * @param source
     * @param destination
     * @param graphic
     * @param event
     *
     * @return
     */
    public T create(S source, State destination, Event event);

    /**
     * @param source
     * @param destination
     * @param graphic
     * @param guard
     *
     * @return
     */
    public T create(S source, State destination, Guard guard);

    /**
     * @param source
     * @param destination
     * @param graphic
     * @param action
     * @param event
     *
     * @return
     */
    public T create(S source, State destination, Action action, Event event);

    /**
     * @param source
     * @param destination
     * @param graphic
     * @param action
     * @param guard
     *
     * @return
     */
    public T create(S source, State destination, Action action, Guard guard);

    /**
     * @param source
     * @param destination
     * @param graphic
     * @param event
     * @param guard
     *
     * @return
     */
    public T create(S source, State destination, Event event, Guard guard);

    /**
     * @param source
     * @param destination
     * @param graphic
     * @param action
     * @param event
     * @param guard
     *
     * @return
     */
    public T create(S source, State destination, Action action, Event event, Guard guard);
}
