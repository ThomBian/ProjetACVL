package model;

import controller.Diagram;
import view.Command;

public class CreateInitialState implements Command {

	@Override
	public void execute() {
		Diagram.getInstance().createInitialState();		
	}

}
