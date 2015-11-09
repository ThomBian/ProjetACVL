package model;

public final class StandardTransition extends Transition<State> {

    public StandardTransition(State source, State destination) {
        super(source, destination);
        // TODO Auto-generated constructor stub
    }

    private Guard guard;
    private Event event;

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

    @Override
    public boolean isStandardTransition() {
        return true;
    }

    public boolean isDefinedTheSameAs(StandardTransition t) {
        if (t.action != null && this.action != null) {
            if (t.action.getName().equals(this.action.getName())) {
                if (t.guard != null && this.guard != null) {
                    if (t.guard.getCondition()
                            .equals(this.guard.getCondition()))
                        return true;
                } else if (t.guard == null && this.guard != null ||
                           t.guard != null && this.guard == null) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(" ");
        if (action != null) {
            s.append(action.getName());
            s.append(" / ");
        }
        if (guard != null) {
            s.append(guard.getCondition());
        }
        s.append(" / ");
        if (event != null) {
            s.append(event.getName());
        }
        return s.toString();
    }
}
