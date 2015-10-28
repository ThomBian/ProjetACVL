package model;

public final class StandardTransition extends Transition<State> {
	
	private Guard guard;
	
	public Guard getGuard() {
		return guard;
	}

	public void setGuard(Guard guard) {
		this.guard = guard;
	}
	

}
