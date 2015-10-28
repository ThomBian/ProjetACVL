package controller;

import javax.swing.UIManager;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import model.InitialState;
import view.MainView;
import view.Style;

import java.util.ArrayList;
import java.util.List;

public class Diagram {

	private static final Diagram instance = new Diagram();

	public static Diagram getInstance() {
		return instance;
	}

	private MainView mainView;
	private mxGraph graph;
	private InitialState initialState = null;
    private List<DiagrammError> errors;

	private Diagram() {
		mxCell cell;
		graph = new mxGraph(){
			  // Make all edges unmovable
			  public boolean isCellMovable(Object cell)
			  {
			    return !getModel().isEdge(cell);
			  }	
			  public boolean isValidDropTarget(Object cell, Object[] cells){
				  if (((mxCell)cell).isVertex() && ((mxCell)cell).getStyle().equals(Style.COMPOSITE))
				  {	  
					  return true;
				  }
				  return false;
			  }
		};
		graph.setAllowDanglingEdges(false);
		graph.setConnectableEdges(false);
		graph.setDropEnabled(true);
        errors = new ArrayList<>();
	}
	
	public void createInitialState() {
		System.out.println(graph.getSelectionCell());
		initialState = new InitialState();
		graph.insertVertex(graph.getDefaultParent(), null, "", 20, 20, 30, 30,
						   Style.INITIAL);
	}

	public void createState(String name) {
		graph.insertVertex(graph.getDefaultParent(), null, name, 20, 20, 80, 30, Style.STATE);
	}

	public void createCompositeState(String name) {
		graph.insertVertex(graph.getDefaultParent(), null, name, 20, 20, 150, 180,Style.COMPOSITE);
		
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
		graph.insertVertex(graph.getDefaultParent(), null, "", 20, 20, 30, 30, Style.FINAL);
	}

	public void validate() {
        mainView.displayValidationWindow(errors);
	}

    public void addError(DiagrammError e){
        errors.add(e);
    }
}
