package model;

import com.mxgraph.model.mxCell;

/**
 * 
 * @author couretn
 *
 * @param <T>
 */
public abstract class Transition<T extends State> {

	protected State destination;
	protected T source;
	protected Guard guard;
	
	private mxCell graphic;
	
	public Transition(T source,State destination) {
		super();
		this.destination = destination;
		this.source = source;
	}
	protected Action action;
	
	public State getDestination() {
		return destination;
	}
	public void setDestination(State destination) {
		this.destination = destination;
	}
	public T getSource() {
		return source;
	}
	public void setSource(T source) {
		this.source = source;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public mxCell getGraphic() {
		return graphic;
	}
	public void setGraphic(mxCell graphic) {
		this.graphic = graphic;
	}
	public Guard getGuard() {
		return this.guard;
	}
	public void setGuard(Guard guard) {
		this.guard = guard;
	}
}
