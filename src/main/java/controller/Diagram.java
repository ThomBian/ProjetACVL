package controller;

import java.util.Hashtable;

import javax.swing.UIManager;

import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import model.InitialState;
import view.MainView;

public class Diagram {
	
	private static final Diagram instance = new Diagram();
	
	public static Diagram getInstance(){
		return instance;
	}
	
	private MainView mainView;
	private mxGraph graph;
	private InitialState initialState = null ;
	private Diagram(){
		graph = new mxGraph();
		
		
		mxStylesheet stylesheet = graph.getStylesheet();

	    // define initial state image style
	    String myStyleName = "initialState";

	    // define image style           
	    Hashtable<String, Object> style = new Hashtable<String, Object>();
	    style.put( mxConstants.STYLE_SHAPE, mxConstants.SHAPE_IMAGE);
	    style.put( mxConstants.STYLE_IMAGE, "file:src/resources/initial.png");
	    style.put( mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);

	    stylesheet.putCellStyle( myStyleName, style);
	}
	public void createInitialState(){
	
		initialState = new InitialState();
		graph.insertVertex(graph.getDefaultParent(), null, "", 20, 20, 30,30,"initialState");
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
	public void createFinalState() {
		graph.insertVertex(graph.getDefaultParent(), null, "", 20, 20, 30,30,"finalState");
	}
		

}
