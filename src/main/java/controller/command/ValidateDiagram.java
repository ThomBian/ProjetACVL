package controller.command;

import controller.Diagram;
public class ValidateDiagram implements Command {

	@Override
	public void execute() {
		Diagram.getInstance().validate();
	}

}
