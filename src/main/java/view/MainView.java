package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.mxgraph.view.mxGraph;

public class MainView {

	private JFrame frame;
	private JMenuBar menuBar;
	private GraphView graphView;
	
	private final Button createInitialStateButton = new Button("Create an initial state");
	private final Button createStateButton = new Button("Create a state");
	private final Button createCompositeStateButton = new Button("Create a composite state");
	private final Button createFinalStateButton = new Button("Create a final state");
	private final Button flattenButton = new Button("Flatten diagram");
	private final Button validateButton = new Button("Validate diagram");


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
		JPanel panel = new JPanel();
		//panel.add(graph);
		frame.add(graphView);
	}

}
