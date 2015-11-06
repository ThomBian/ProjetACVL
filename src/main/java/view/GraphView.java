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
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel.mxChildChange;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxStylesheet;

import controller.Diagram;
import model.CompositeState;
import model.FinalState;
import model.InitialState;
import model.NamedState;
import model.SimpleState;
import model.State;
import model.Transition;

public class GraphView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6717176422645301601L;
	private CustomMxGraph graph;

	private Map<State, mxCell> linkedStates = new HashMap<State, mxCell>();
	private Map<Transition<State>, mxCell> linkedTransitions = new HashMap<Transition<State>, mxCell>();
	Map<State, mxCell> tmpStates = null;
	private int initialX = 20;
	private int initialY = 20;
	
	private int getInitialX(){
		if(initialX >= 200) initialX = 20;
		else initialX += 20;
		return initialX;
	}
	
	private int getInitialY(){
		if(initialY >= 200) initialY = 20;
		else initialY += 20;
		return initialY;
	}
	

	public GraphView() {
		super();
		graph = new CustomMxGraph();
		graph.setAllowDanglingEdges(false);
		graph.setConnectableEdges(false);
		graph.setDropEnabled(true);
		graph.setAllowLoops(true);
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
				State sourceState = getStateFromMxCell(source);
				State targetState = getStateFromMxCell(target);
				d.addTransitionToModel(sourceState, targetState, newTransition);
			}
		});

		graph.addListener(mxEvent.LABEL_CHANGED, new mxEventSource.mxIEventListener() {
			public void invoke(Object sender, mxEventObject evt) {
				mxCell cell = (mxCell) evt.getProperty("cell");
				String label = (String) evt.getProperty("label");
				Diagram d = Diagram.getInstance();
				if (cell.isVertex()) {
					NamedState state = (NamedState) getStateFromMxCell(cell);
					List<State> states = d.getAllStates();
					d.updateStateName(state, label, states);
				} else {
					Transition<State> transition = getTransitionFromMxCell(cell);
					d.updateTransitionName(transition, label);
				}
			}
		});

		graph.addListener(mxEvent.REMOVE_CELLS, new mxEventSource.mxIEventListener() {
			public void invoke(Object sender, mxEventObject evt) {
				if (!graph.isReactToDeleteEvent()) {
					return;
				}
				try {
					Object[] cells = (Object[]) evt.getProperty("cells");
					Diagram d = Diagram.getInstance();
					for (Object cell : cells) {
						if (((mxCell) cell).isVertex()) {
							d.removeState(getStateFromMxCell((mxCell) cell), true);
						} else {
							d.removeTransition(getTransitionFromMxCell((mxCell) cell));
						}
					}
				} catch (Exception e) {

				}
			}
		});

		graph.getModel().addListener(mxEvent.CHANGE, new mxEventSource.mxIEventListener() {
			public void invoke(Object sender, mxEventObject evt) {
				// lets detect DROP events
				ArrayList<?> changes = (ArrayList<?>) evt.getProperty("changes");
				for (Object o : changes) {
					if (o instanceof mxChildChange) {
						mxChildChange childChange = (mxChildChange) o;
						mxCell dropped = (mxCell) childChange.getChild();
						mxCell previousParent = (mxCell) childChange.getPrevious();
						mxCell parent = (mxCell) childChange.getParent();
						// previousParent == null = insertion / parent == null =
						// deletion
						if (previousParent != null && parent != null && !dropped.isEdge()) {
							// we are sure its a drop
							Diagram d = Diagram.getInstance();
							State s = getStateFromMxCell(dropped);
							if (s != null) {
								d.dropStateIntoCompositeState(getStateFromMxCell(dropped),
										(CompositeState) getStateFromMxCell(parent));
							}
						}
					}
				}
			}
		});
	}

	public CustomMxGraph getGraph() {
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
		edge.put(mxConstants.STYLE_STROKECOLOR, "#000000"); // default is
															// #6482B9
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

	private mxGeometry getPreviousGeometry(State s) {
		if (getTmpStates() != null && getTmpStates().containsKey(s)) {
			mxCell cell = getTmpStates().get(s);
			// Compute if it was in a parent before
			mxICell parent = cell.getParent();
			while(parent != null && parent.getGeometry() != null){
				cell.getGeometry().setX(cell.getGeometry().getX() + parent.getGeometry().getX());
				cell.getGeometry().setY(cell.getGeometry().getY() + parent.getGeometry().getY());
				parent = parent.getParent();
			}
			return cell.getGeometry();
		} else {
			return null;
		}
	}

	public void insertState(InitialState initialState, CompositeState parent) {
		mxGeometry geo = getPreviousGeometry(initialState);
		if (geo == null) {
			geo = new mxGeometry(getInitialX(), getInitialY(), 30, 30);
			geo.setRelative(false);
		}

		Object pCell = linkedStates.get(parent);
		if (parent == null)
			pCell = getGraph().getDefaultParent();
		mxCell vertex = (mxCell) getGraph().createVertex(pCell, null, "", geo.getX(), geo.getY(), geo.getWidth(),
				geo.getHeight(), Style.INITIAL);
		getGraph().addCell(vertex);
		getGraph().setSelectionCell(vertex);
		linkedStates.put(initialState, vertex);
	}

	public void insertState(InitialState initialState) {
		insertState(initialState, null);
	}

	public void insertState(FinalState finalState, CompositeState parent) {
		mxGeometry geo = getPreviousGeometry(finalState);
		if (geo == null) {
			geo = new mxGeometry(getInitialX(), getInitialY(), 30, 30);
			geo.setRelative(false);
		}

		Object pCell = linkedStates.get(parent);
		if (parent == null)
			pCell = getGraph().getDefaultParent();
		mxCell vertex = (mxCell) getGraph().createVertex(pCell, null, "", geo.getX(), geo.getY(), geo.getWidth(),
				geo.getHeight(), Style.FINAL);
		getGraph().addCell(vertex);
		getLinkedStates().put(finalState, vertex);
		getGraph().setSelectionCell(vertex);
	}

	public void insertState(FinalState finalState) {
		insertState(finalState, null);
	}

	public void insertState(SimpleState simpleState, CompositeState parent) {
		mxGeometry geo = getPreviousGeometry(simpleState);
		if (geo == null) {
			geo = new mxGeometry(getInitialX(), getInitialY(), 80, 30);
			geo.setRelative(false);
		}
		Object pCell = linkedStates.get(parent);
		if (parent == null)
			pCell = getGraph().getDefaultParent();
		mxCell vertex = (mxCell) getGraph().createVertex(pCell, null, simpleState.getName(), geo.getX(), geo.getY(),
				geo.getWidth(), geo.getHeight(), Style.STATE);
		getGraph().addCell(vertex);
		linkedStates.put(simpleState, vertex);
		getGraph().setSelectionCell(vertex);
	}

	public void insertState(SimpleState simpleState) {
		insertState(simpleState, null);
	}

	public void insertState(CompositeState compositeState, CompositeState parent) {
		mxGeometry geo = getPreviousGeometry(compositeState);
		if (geo == null) {
			geo = new mxGeometry(getInitialX(), getInitialY(), 300, 300);
			geo.setRelative(false);
		}

		Object pCell = linkedStates.get(parent);
		if (parent == null)
			pCell = getGraph().getDefaultParent();
		mxCell vertex = (mxCell) getGraph().createVertex(pCell, null, compositeState.getName(), geo.getX(), geo.getY(),
				geo.getWidth(), geo.getHeight(), Style.COMPOSITE);
		getGraph().addCell(vertex);
		linkedStates.put(compositeState, vertex);
		getGraph().setSelectionCell(vertex);
	}

	public void insertState(CompositeState compositeState) {
		insertState(compositeState, null);
	}

	public State getStateFromMxCell(Object cell) {
		for (State o : linkedStates.keySet()) {
			if (linkedStates.get(o).equals(cell)) {
				return o;
			}
		}
		return null;
	}

	public Transition<State> getTransitionFromMxCell(mxCell cell) {
		for (Transition<State> o : getLinkedTransitions().keySet()) {
			if (getLinkedTransitions().get(o).equals(cell)) {
				return o;
			}
		}
		return null;
	}

	public void insertTransition(Transition<State> t) {

		mxCell sourceCell = getLinkedStates().get(t.getSource()), destCell = getLinkedStates().get(t.getDestination());
		mxCell edge = (mxCell) getGraph().createEdge(getGraph().getDefaultParent(), null, "", sourceCell, destCell,
				Style.EDGE);
		edge = (mxCell) getGraph().addEdge(edge, getGraph().getDefaultParent(), sourceCell, destCell, null);

	}

	public Map<State, mxCell> getTmpStates() {
		return tmpStates;
	}

	public void setTmpStates(Map<State, mxCell> tmpStates) {
		this.tmpStates = tmpStates;
	}

	public void setLinkedStates(Map<State, mxCell> linkedStates) {
		this.linkedStates = linkedStates;
	}

	public void setLinkedTransitions(Map<Transition<State>, mxCell> linkedTransitions) {
		this.linkedTransitions = linkedTransitions;
	}

	public Map<State, mxCell> getLinkedStates() {
		return linkedStates;
	}

	public Map<Transition<State>, mxCell> getLinkedTransitions() {
		return linkedTransitions;
	}

}
