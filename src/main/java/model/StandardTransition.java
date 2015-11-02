package model;

public final class StandardTransition extends Transition<State> {
	
	

	public StandardTransition(State source, State destination) {
		super(source, destination);
		// TODO Auto-generated constructor stub
	}

	private Guard guard;
	
	public Guard getGuard() {
		return guard;
	}

	public void setGuard(Guard guard) {
		this.guard = guard;
	}
	

}
