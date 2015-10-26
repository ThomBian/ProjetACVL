package view;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JPanel;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxPerimeter;
import com.mxgraph.view.mxStylesheet;

public class GraphView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6717176422645301601L;

	public GraphView(mxGraph graph) {
		super();
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		setLayout(new BorderLayout());
		add(graphComponent, BorderLayout.CENTER);
		initializeAllStyle(graph.getStylesheet());
		
		// Allow deletion of graph components
		new CustomKeyboardHandler(graphComponent);
		
		// Example of possible listeners
		/*
		// Detects edge connection
		graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener() {
			public void invoke(Object sender, mxEventObject evt) {
				System.out.println("edge created =" + evt.getProperty("cell"));
			}
		});
		*/

		graph.addListener(mxEvent.REMOVE_CELLS, new mxEventSource.mxIEventListener() {
			public void invoke(Object sender, mxEventObject evt) {
				
				System.out.println("cells removed =" + evt.getProperty("cells"));
				System.out.println("transition removed =" + evt.getProperty("includeEdges"));
			}
		});


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
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_IMAGE);
		style.put(mxConstants.STYLE_IMAGE, "file:src/resources/" + picName);
		style.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
		return style;
	}

}
