/**
 * 
 */
package controller.command;

import controller.Diagram;

/**
 * @author ncouret
 *
 */
public class CreateState implements Command {

	/* (non-Javadoc)
	 * @see controller.command.Command#execute()
	 */
	@Override
	public void execute() {
		Diagram.getInstance().createState("state");

	}

}
