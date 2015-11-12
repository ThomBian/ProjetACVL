package view;

import controller.command.*;
import model.error.DiagramError;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainView {

    private JFrame    frame;
    private JMenuBar  menuBar;
    private GraphView graphView;

    private final Button createInitialStateButton   =
            new Button("Create an initial state", "initial.png", new CreateInitialState());
    private final Button createStateButton          = new Button("Create a state", new CreateState());
    private final Button createCompositeStateButton =
            new Button("Create a composite state", new CreateCompositeState());
    private final Button createFinalStateButton     =
            new Button("Create a final state", "final.png", new CreateFinalState());
    private final Button flattenButton              =
            new Button("Flatten diagram", "steamroller.png", new FlattenDiagram());
    private final Button validateButton             =
            new Button("Validate diagram", "check-icon.png", new ValidateDiagram());

    public void displayValidationWindow(List<DiagramError> dErrors) {
        if (dErrors.size() == 0) {
            //display ok
            graphView.informUser("Ok !");
        } else {
            //display all errors
            String errors = "Errors found : \n";
            for (DiagramError error : dErrors) {
                errors += error.toString() + "\n";
            }
            graphView.informUser(errors);
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    public GraphView getGraph() {
        return graphView;
    }

    /**
     * Create the application.
     */
    public MainView() {
        initialize();
        initializeEmptyGraph();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame("Diagram editor");

        frame.setBounds(100, 100, 950, 450);
        frame.setMinimumSize(new Dimension(1050, 500));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        menuBar.add(createInitialStateButton);
        menuBar.add(createStateButton);
        menuBar.add(createCompositeStateButton);
        menuBar.add(createFinalStateButton);
        menuBar.add(flattenButton);
        menuBar.add(validateButton);
        frame.setJMenuBar(menuBar);
    }

    private void initializeEmptyGraph() {
        graphView = new GraphView();
        frame.add(graphView);
    }
}
