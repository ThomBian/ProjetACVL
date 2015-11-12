package controller.command;

import controller.Diagram;

public class CreateFinalState implements Command {

    @Override
    public void execute() {
        Diagram.getInstance().createFinalState();
    }
}
