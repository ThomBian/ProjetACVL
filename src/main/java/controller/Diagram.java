package controller;

import java.awt.EventQueue;

import javax.swing.UIManager;

import com.mxgraph.view.mxGraph;


import view.MainView;

public class Diagram {
	
	private static final Diagram instance = new Diagram();
	
	public static Diagram getInstance(){
		return instance;
	}
	
	private MainView mainView;
	private mxGraph graph;
	
	private Diagram(){
		graph = new mxGraph();
	}
	public void createInitialState(){
		graph.insertVertex(graph.getDefaultParent(), null, "Je suis initial", 20, 20, 80,30);	
	}
	
	public void createState(String name){
		graph.insertVertex(graph.getDefaultParent(), null, name, 20, 20, 80,30);	
	}
	public void createCompositeState(String name){
		graph.insertVertex(graph.getDefaultParent(), null, name, 20, 20, 80,30);	
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
		

}
