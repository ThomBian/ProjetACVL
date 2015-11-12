package model;

public final class StandardTransition extends Transition<State> {

    public StandardTransition(State source, State destination) {
        super(source, destination);
        // TODO Auto-generated constructor stub
    }

    private Guard guard = null;
    private Event event = null ;

    public Guard getGuard() {
        return guard;
    }

    public void setGuard(Guard guard) {
        this.guard = guard;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String toString() {
        if (this.getEvent() != null) {
            if (getGuard() != null) {
                return getAction().getName() + " / " + getEvent().getName() + " / " + getGuard().getCondition();
            } else {
                return getAction().getName() + " / " + getEvent().getName();
            }
        } else if (getGuard() != null) {
            return getAction().getName() + " / " + getGuard().getCondition();
        } else {
            return getAction().getName();
        }
    }

    @Override
    public boolean isStandardTransition() {
        return true;
    }

    public boolean isDefinedTheSameAs(StandardTransition t) {
        if (t.action != null && this.action != null) {
            if (t.action.getName().equals(this.action.getName())) {
                if (t.guard != null && this.guard != null) {
                    if (t.guard.getCondition().equals(this.guard.getCondition()))
                        return true;
                } else if (t.guard == null && this.guard != null || t.guard != null && this.guard == null) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}
