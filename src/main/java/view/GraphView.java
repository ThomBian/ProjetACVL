package view;

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
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GraphView extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -6717176422645301601L;
    private CustomMxGraph graph;

    private Map<State, mxCell>             linkedStates      = new HashMap<State, mxCell>();
    private Map<Transition<State>, mxCell> linkedTransitions = new HashMap<Transition<State>, mxCell>();
    private Map<State, mxCell>             tmpStates         = null;
    private int                            initialX          = 20;
    private int                            initialY          = 20;

    private int getInitialX() {
        if (initialX >= 200)
            initialX = 20;
        else
            initialX += 20;
        return initialX;
    }

    private int getInitialY() {
        if (initialY >= 200)
            initialY = 20;
        else
            initialY += 20;
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
                d.addTransitionAndRefreshView(sourceState, targetState, newTransition);
            }
        });

        graph.addListener(mxEvent.LABEL_CHANGED, new mxEventSource.mxIEventListener() {
            public void invoke(Object sender, mxEventObject evt) {
            	
            	if(getGraph().isReactToUpdateLabelEvent()){
            		 mxCell cell = (mxCell) evt.getProperty("cell");
                     String label = (String) evt.getProperty("label");
                     Diagram d = Diagram.getInstance();
                     if (cell.isVertex()) {
                         NamedState state = (NamedState) getStateFromMxCell(cell);
                         List<State> states = d.getAllStates();
                         d.updateStateName(state, label, states);
                     } else {
                         Transition<State> transition = getTransitionFromMxCell(cell);
                         d.updateTransitionName(transition, label, true);
                     }
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
        
        style.put(mxConstants.STYLE_IMAGE, getClass().getClassLoader().getResource(picName));
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
            while (parent != null && parent.getGeometry() != null) {
                cell.getGeometry().setX(cell.getGeometry().getX() + parent.getGeometry().getX());
                cell.getGeometry().setY(cell.getGeometry().getY() + parent.getGeometry().getY());
                parent = parent.getParent();
            }
            return cell.getGeometry();
        } else {
            return null;
        }
    }

    private void addVertexToGraph(State s, mxCell vertex, boolean isSelectByDefault) {
        getGraph().addCell(vertex);
        if (isSelectByDefault)
            getGraph().setSelectionCell(vertex);
        linkedStates.put(s, vertex);
    }

    private mxCell createVertex(Object parentCell, mxGeometry geo, String label, String style) {
        return (mxCell) getGraph()
                .createVertex(parentCell, null, label, geo.getX(), geo.getY(), geo.getWidth(), geo.getHeight(), style);
    }

    public void insertState(InitialState initialState, CompositeState parent) {
        mxGeometry geo = getPreviousGeometry(initialState);
        geo = getGeometry(geo, 30, 30);

        Object pCell = linkedStates.get(parent);
        if (parent == null)
            pCell = getGraph().getDefaultParent();
        mxCell vertex = createVertex(pCell, geo, "", Style.INITIAL);
        addVertexToGraph(initialState, vertex, true);
    }

    public void insertState(InitialState initialState) {
        insertState(initialState, null);
    }

    public void insertState(FinalState finalState, CompositeState parent) {
        mxGeometry geo = getPreviousGeometry(finalState);
        geo = getGeometry(geo, 30, 30);

        Object pCell = linkedStates.get(parent);
        if (parent == null)
            pCell = getGraph().getDefaultParent();
        mxCell vertex = createVertex(pCell, geo, "", Style.FINAL);
        addVertexToGraph(finalState, vertex, true);
    }

    public void insertState(FinalState finalState) {
        insertState(finalState, null);
    }

    public void insertState(SimpleState simpleState, CompositeState parent) {
        mxGeometry geo = getPreviousGeometry(simpleState);
        geo = getGeometry(geo, 80, 30);

        Object pCell = linkedStates.get(parent);
        if (parent == null)
            pCell = getGraph().getDefaultParent();
        mxCell vertex = createVertex(pCell, geo, simpleState.getName(), Style.STATE);
        addVertexToGraph(simpleState, vertex, true);
    }

    public void insertState(SimpleState simpleState) {
        insertState(simpleState, null);
    }

    public void insertState(CompositeState compositeState, CompositeState parent) {
        mxGeometry geo = getPreviousGeometry(compositeState);
        geo = getGeometry(geo, 300, 300);

        Object pCell = linkedStates.get(parent);
        if (parent == null)
            pCell = getGraph().getDefaultParent();
        mxCell vertex = createVertex(pCell, geo, compositeState.getName(), Style.COMPOSITE);
        addVertexToGraph(compositeState, vertex, true);
    }

    public void insertState(CompositeState compositeState) {
        insertState(compositeState, null);
    }

    private mxGeometry getGeometry(mxGeometry geo, int width, int height) {
        if (geo == null) {
            geo = new mxGeometry(getInitialX(), getInitialY(), width, height);
            geo.setRelative(false);
        }
        return geo;
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
        mxCell edge = (mxCell) getGraph()
                .createEdge(getGraph().getDefaultParent(), null, t.toString(), sourceCell, destCell, Style.EDGE);
        edge = (mxCell) getGraph().addEdge(edge, getGraph().getDefaultParent(), sourceCell, destCell, null);
        getLinkedTransitions().put(t, edge);
    }

    public void updateStateLabel(NamedState namedState) {
        getLinkedStates().get(namedState).setValue(namedState.getName());
    }

    public void updateTransitionLabel(Transition<State> transition) {
    	graph.setReactToUpdateLabelEvent(false);
        getLinkedTransitions().get(transition).setValue(transition.toString());
        graph.setReactToUpdateLabelEvent(true);
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
