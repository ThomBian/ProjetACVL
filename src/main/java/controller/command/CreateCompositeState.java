package controller.command;

import controller.Diagram;

public class CreateCompositeState implements Command {

	@Override
	public void execute() {
		Diagram.getInstance().createCompositeState("Default Name");		
	}

}
