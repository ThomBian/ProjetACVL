package view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel.mxChildChange;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import controller.Diagram;
import model.CompositeState;
import model.NamedState;
import model.State;
import model.Transition;

public class GraphView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6717176422645301601L;
	private mxGraph graph;
	// TODO use those attributes instead of the one in diagram !
	// private Map<State, mxCell> linkedStates = new HashMap<State, mxCell>();
	// private Map<Transition, mxCell> linkedTransitions = new HashMap<Transition, mxCell>();
	public GraphView() {
		super();
		graph = new CustomMxGraph();
		graph.setAllowDanglingEdges(false);
		graph.setConnectableEdges(false);
		graph.setDropEnabled(true);
        com.mxgraph.swing.util.mxGraphTransferable.enableImageSupport = false;
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		setLayout(new BorderLayout());
		add(graphComponent, BorderLayout.CENTER);
		initializeAllStyle(graph.getStylesheet());
		
		// Allow deletion of graph components
		new CustomKeyboardHandler(graphComponent);
		
		// Example of possible listeners
		
		// Detects edge connection
		graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener() {
			public void invoke(Object sender, mxEventObject evt) {
				mxCell newTransition = (mxCell) evt.getProperty("cell");
				mxICell target = newTransition.getTarget();
				mxICell source = newTransition.getSource();
				Diagram d = Diagram.getInstance();
				State sourceState = d.getStateFromMxCell(source);
				State targetState = d.getStateFromMxCell(target);
				d.addTransitionToModel(sourceState,targetState,newTransition);
			}
		});
		
		graph.addListener(mxEvent.LABEL_CHANGED, new mxEventSource.mxIEventListener() {
			public void invoke(Object sender, mxEventObject evt) {
				mxCell cell = (mxCell) evt.getProperty("cell");
				String label = (String) evt.getProperty("label");
				Diagram d = Diagram.getInstance();
				if (cell.isVertex()) {					
					NamedState state = (NamedState) d.getStateFromMxCell(cell);
					List<State> states = d.getAllStates();
					d.updateStateName(state, label, states);
				} else {
					Transition<State> transition = d.getTransitionFromMxCell(cell);
					d.updateTransitionName(transition, label);
				}
			}
		});

		graph.addListener(mxEvent.REMOVE_CELLS, new mxEventSource.mxIEventListener() {
			public void invoke(Object sender, mxEventObject evt) {
				try{
					Object [] cells = (Object[]) evt.getProperty("cells");
					Diagram d = Diagram.getInstance();
					for(Object cell : cells){
						if(((mxCell) cell).isVertex()){
							d.removeState(d.getStateFromMxCell((mxCell)cell));
						}else{
							d.removeTransitionFromModel(d.getTransitionFromMxCell((mxCell)cell));
						}
					}
					//System.out.println("transition removed =" + evt.getProperty("includeEdges"));
				}catch(Exception e){
					
				}
				
				
			}
		});
		
			graph.getModel().addListener(mxEvent.CHANGE, new mxEventSource.mxIEventListener() {
				public void invoke(Object sender, mxEventObject evt) {
					
					// lets detect DROP events
					ArrayList<?> changes = (ArrayList<?>)evt.getProperty("changes");
					for(Object o : changes){
					 if (o instanceof mxChildChange)
					    {
					    	mxChildChange childChange = (mxChildChange) o;
					    	mxCell dropped = (mxCell) childChange.getChild();
					        mxCell previousParent = (mxCell) childChange.getPrevious();
					        mxCell parent = (mxCell) childChange.getParent();
					        // previousParent == null = insertion / parent == null = deletion
					        if(previousParent != null && parent != null && !dropped.isEdge()){
					        	// we are sure its a drop
					        	Diagram d = Diagram.getInstance();
					        	State s = d.getStateFromMxCell(dropped);
					        	if(s != null){
					        		d.dropStateIntoCompositeState(d.getStateFromMxCell(dropped), (CompositeState) d.getStateFromMxCell(parent));
					        	}
								
					        }
					    }
					}
				}
			});


	}

	public mxGraph getGraph() {
		return graph;
	}

	private void initializeAllStyle(mxStylesheet styleSheet) {
		styleSheet.putCellStyle(Style.INITIAL, getPictureStyle("initial.png"));
		styleSheet.putCellStyle(Style.FINAL, getPictureStyle("final.png"));
		styleSheet.putCellStyle(Style.COMPOSITE, getCompositeStyle());
		styleSheet.putCellStyle(Style.STATE, getNormalStyle());
		applyEdgeDefaults(styleSheet);
	}
	private void applyEdgeDefaults(mxStylesheet styleSheet) {
	    // Settings for edges
	    Map<String, Object> edge = new HashMap<String, Object>();
	    edge.put(mxConstants.STYLE_ROUNDED, true);
	    edge.put(mxConstants.STYLE_ORTHOGONAL, false);
	    edge.put(mxConstants.STYLE_EDGE, "elbowEdgeStyle");
	    edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
	    edge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
	    edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
	    edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
	    edge.put(mxConstants.STYLE_STROKECOLOR, "#000000"); // default is #6482B9
	    edge.put(mxConstants.STYLE_FONTCOLOR, "#446299");
	    styleSheet.putCellStyle(Style.EDGE, edge);
	    styleSheet.setDefaultEdgeStyle(edge);
	}
	private Map<String, Object> getNormalStyle() {
		Hashtable<String, Object> style = new Hashtable<String, Object>();
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		return style;
	}

	private Hashtable<String, Object> getCompositeStyle() {
		// define image style
		Hashtable<String, Object> style = new Hashtable<String, Object>();
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		// style.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION,
		// mxConstants.ALIGN_TOP);
		style.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_TOP);

		return style;
	}

	private Hashtable<String, Object> getPictureStyle(String picName) {
		// define image style
		Hashtable<String, Object> style = new Hashtable<String, Object>();
		style.put(mxConstants.STYLE_NOLABEL, "1");
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_IMAGE);
		style.put(mxConstants.STYLE_IMAGE, "file:src/resources/" + picName);
		style.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
		return style;
	}

	public void informUser(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

}
