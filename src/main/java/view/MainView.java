package view;

import javax.swing.*;

import com.mxgraph.view.mxGraph;

import controller.DiagramError;
import controller.command.CreateCompositeState;
import controller.command.CreateFinalState;
import controller.command.CreateInitialState;
import controller.command.CreateState;
import controller.command.FlattenDiagram;
import controller.command.ValidateDiagram;

import java.util.List;

public class MainView {

	private JFrame frame;
	private JMenuBar menuBar;
	private GraphView graphView;

	private final Button createInitialStateButton = new Button("Create an initial state", "initial.png",
			new CreateInitialState());
	private final Button createStateButton = new Button("Create a state", new CreateState());
	private final Button createCompositeStateButton = new Button("Create a composite state",
			new CreateCompositeState());
	private final Button createFinalStateButton = new Button("Create a final state", "final.png",
			new CreateFinalState());
	private final Button flattenButton = new Button("Flatten diagram", new FlattenDiagram());
	private final Button validateButton = new Button("Validate diagram", new ValidateDiagram());


	public void displayValidationWindow(List<DiagramError> errors) {
        if (errors.size() == 0){
            //display ok
            System.out.print("OK !");
        } else {
            //display all errors
            for (DiagramError error : errors){
                System.out.println(error.toString());
            }

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
