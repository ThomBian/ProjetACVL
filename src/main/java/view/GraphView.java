package view;

import java.awt.BorderLayout;
import java.util.Hashtable;

import javax.swing.JPanel;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
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
	}
	private void initializeAllStyle(mxStylesheet styleSheet) {
		styleSheet.putCellStyle("initial", getStyle("initial.png"));
		styleSheet.putCellStyle("final", getStyle("final.png"));

	}

	private Hashtable<String, Object> getStyle(String picName) {
		// define image style
		Hashtable<String, Object> style = new Hashtable<String, Object>();
		style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_IMAGE);
		style.put(mxConstants.STYLE_IMAGE, "file:src/resources/" + picName);
		style.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
		return style;
	}


}
