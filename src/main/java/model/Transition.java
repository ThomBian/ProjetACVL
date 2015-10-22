package model;

public abstract class Transition<T extends State> {

	private State destination;
	private T source;
	
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
}
