package controller.command;

import controller.Diagram;

public class CreateInitialState implements Command {

	@Override
	public void execute() {
		Diagram.getInstance().createInitialState();
	}

}
