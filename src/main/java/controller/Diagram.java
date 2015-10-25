package controller;

import javax.swing.UIManager;

import model.InitialState;
import view.MainView;

import com.mxgraph.view.mxGraph;

public class Diagram {

	private static final Diagram instance = new Diagram();

	public static Diagram getInstance() {
		return instance;
	}

	private MainView mainView;
	private mxGraph graph;
	private InitialState initialState = null;

	private Diagram() {
		graph = new mxGraph();

	}
	
	public void createInitialState() {

		initialState = new InitialState();
		graph.insertVertex(graph.getDefaultParent(), null, "", 20, 20, 30, 30, "initial");
	}

	public void createState(String name) {
		graph.insertVertex(graph.getDefaultParent(), null, name, 20, 20, 80, 30, "normal");
	}

	public void createCompositeState(String name) {
		graph.insertVertex(graph.getDefaultParent(), null, name, 20, 20, 150, 180,"composite");
		
	}

	public void launchApplication() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			mainView = new MainView(graph);
			mainView.getFrame().setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createFinalState() {
		graph.insertVertex(graph.getDefaultParent(), null, "", 20, 20, 30, 30, "final");
	}

}
