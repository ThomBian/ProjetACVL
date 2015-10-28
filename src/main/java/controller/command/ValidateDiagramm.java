package controller.command;

import controller.Diagram;
public class ValidateDiagramm implements Command {

	@Override
	public void execute() {
		Diagram.getInstance().validate();
	}

}
