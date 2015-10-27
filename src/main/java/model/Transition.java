package model;


/**
 * 
 * @author couretn
 *
 * @param <T>
 */
public abstract class Transition<T extends State> {

	protected State destination;
	protected T source;
	protected Action action;
	protected String mSource, mDest;
	
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
	public String getmSource() {
		return mSource;
	}
	public void setmSource(String mSource) {
		this.mSource = mSource;
	}
	public String getmDest() {
		return mDest;
	}
	public void setmDest(String mDest) {
		this.mDest = mDest;
	}
}
