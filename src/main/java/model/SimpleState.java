package model;

/**
 * 
 *
 */
public final class SimpleState extends NamedState {

	public SimpleState(String name) {
		super(name);
	}

	public String toString(){
		return "State : " + this.getName();
	}
}
