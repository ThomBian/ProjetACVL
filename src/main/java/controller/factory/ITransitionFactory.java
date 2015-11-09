/**
 * 
 */
package controller.factory;

import com.mxgraph.model.mxCell;

import model.Action;
import model.Event;
import model.Guard;
import model.State;
import model.Transition;

/**
 * @author ncouret
 *
 */
public interface ITransitionFactory<T extends Transition<S>, S extends State> {
	
	/**
	 * 
	 * @param source
	 * @param destination
	 * @param graphic
	 * @return
	 */
	public T create(S source, State destination, mxCell graphic);
	
	/**
	 * 
	 * @param source
	 * @param destination
	 * @param graphic
	 * @param action
	 * @return
	 */
	public T create(S source, State destination, mxCell graphic, Action action);
	
	/**
	 * 
	 * @param source
	 * @param destination
	 * @param graphic
	 * @param event
	 * @return
	 */
	public T create(S source, State destination, mxCell graphic, Event event);
	
	/**
	 * 
	 * @param source
	 * @param destination
	 * @param graphic
	 * @param guard
	 * @return
	 */
	public T create(S source, State destination, mxCell graphic, Guard guard);
	
	/**
	 * 
	 * @param source
	 * @param destination
	 * @param graphic
	 * @param action
	 * @param event
	 * @return
	 */
	public T create(S source, State destination, mxCell graphic, Action action, Event event);
	
	/**
	 * 
	 * @param source
	 * @param destination
	 * @param graphic
	 * @param action
	 * @param guard
	 * @return
	 */
	public T create(S source, State destination, mxCell graphic, Action action, Guard guard);
	
	/**
	 * 
	 * @param source
	 * @param destination
	 * @param graphic
	 * @param event
	 * @param guard
	 * @return
	 */
	public T create(S source, State destination, mxCell graphic, Event event, Guard guard);
	
	/**
	 * 
	 * @param source
	 * @param destination
	 * @param graphic
	 * @param action
	 * @param event
	 * @param guard
	 * @return
	 */
	public T create(S source, State destination, mxCell graphic, Action action, Event event, Guard guard);

}
