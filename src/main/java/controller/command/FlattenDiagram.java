package controller.command;

import controller.Diagram;

public class FlattenDiagram implements Command {

    @Override
    public void execute() {
        Diagram.getInstance().flatten();
    }
}
