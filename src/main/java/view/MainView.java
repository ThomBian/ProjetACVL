package view;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import com.mxgraph.view.mxGraph;

import controller.command.CreateCompositeState;
import controller.command.CreateFinalState;
import controller.command.CreateInitialState;
import controller.command.CreateState;
import controller.command.FlattenDiagramm;
import controller.command.ValidateDiagramm;

public class MainView {

	private JFrame frame;
	private JMenuBar menuBar;
	private GraphView graphView;
	
	private final Button createInitialStateButton = new Button("Create an initial state", "initial.png", new CreateInitialState());
	private final Button createStateButton = new Button("Create a state", "final.png", new CreateState());
	private final Button createCompositeStateButton = new Button("Create a composite state", new CreateCompositeState());
	private final Button createFinalStateButton = new Button("Create a final state", new CreateFinalState());
	private final Button flattenButton = new Button("Flatten diagram", new FlattenDiagramm());
	private final Button validateButton = new Button("Validate diagram", new ValidateDiagramm());



	public JFrame getFrame() {
		return frame;
	}

	public GraphView getGraph() {
		return graphView;
	}

	/**
	 * Create the application.
	 */
	public MainView(mxGraph graph) {
		initialize();
		initializeEmptyGraph(graph);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Diagram editor");
		frame.setBounds(100, 100, 750, 400);
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
	
	private void initializeEmptyGraph(mxGraph graphData){
		graphView = new GraphView(graphData);
		frame.add(graphView);
	}
	

}
