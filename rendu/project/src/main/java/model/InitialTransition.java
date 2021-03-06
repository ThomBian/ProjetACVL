package model;

public final class InitialTransition extends Transition<InitialState> {

    public InitialTransition(InitialState source, State destination) {
        super(source, destination);
    }

    @Override
    public boolean isInitialTransition() {
        return true;
    }
}
