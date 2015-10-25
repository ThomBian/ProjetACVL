package controller.command;

import controller.Diagram;

public class CreateState implements Command {

	@Override
	public void execute() {
		Diagram.getInstance().createState("Default");
	}

}
