package model;

public abstract class Transition<T extends State> {

	private State destination;
	private T source;
}
