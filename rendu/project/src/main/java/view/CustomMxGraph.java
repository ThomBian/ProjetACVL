package view;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.view.mxGraph;
import controller.Diagram;
import model.CompositeState;
import model.State;

public class CustomMxGraph extends mxGraph {

    private boolean reactToDeleteEvent = true;
    private boolean reactToUpdateLabelEvent = true;
    // Make all edges unmovable
    @Override
    public boolean isCellMovable(Object cell) {
        return !getModel().isEdge(cell);
    }

    // Disallow transition to be moved to another state
    @Override
    public boolean isCellDisconnectable(Object cell, Object terminal, boolean source) {
        return false;
    }

    @Override
    public boolean isValidDropTarget(Object cell, Object[] cells) {
        try {
            if (cells.length == 1 && ((mxCell) cell).isVertex() && ((mxCell) cell).getStyle().equals(Style.COMPOSITE)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isReactToDeleteEvent() {
        return reactToDeleteEvent;
    }

    public void setReactToDeleteEvent(boolean reactToDeleteEvent) {
        this.reactToDeleteEvent = reactToDeleteEvent;
    }

    @Override
    public boolean isValidConnection(Object source, Object target) {

        if (((mxCell) target).getStyle().equals(Style.INITIAL))
            return false;
        else if (((mxCell) target).getStyle().equals(Style.COMPOSITE) &&
                 ((mxCell) source).getStyle().equals(Style.INITIAL)) {
            CompositeState c = (CompositeState) Diagram.getInstance().getView().getGraph().getStateFromMxCell(target);
            State s = Diagram.getInstance().getView().getGraph().getStateFromMxCell(source);
            for (State cur : c.getStates()) {
                if (cur.equals(s)) {
                    return false;
                }
            }
        }
        // Disallow transition to an initial state
        if (((mxCell) source).getStyle().equals(Style.INITIAL)) {
            // at this moment it really have one ;)
            if (((mxCell) source).getEdgeCount() == 2)
                return false;
            // disallow transition from an initial state to another state not being of direct son of its parent
            if (((mxCell) source).getParent() != null) {
                mxCell parent = (mxCell) ((mxCell) source).getParent();
                if (parent.getStyle() != null && ((mxCell) source).getParent().getStyle().equals(Style.COMPOSITE)) {
                    CompositeState c =
                            (CompositeState) Diagram.getInstance().getView().getGraph().getStateFromMxCell(parent);
                    State s = Diagram.getInstance().getView().getGraph().getStateFromMxCell(target);
                    if (!c.getStates().contains(s))
                        return false;
                }
            }
        } else if (((mxCell) target).isEdge())
            return false;
        return true;
    }

    // Disallow final state to have outgoing transition
    @Override
    public boolean isValidSource(Object cell) {
        try {
            if (((mxCell) cell).getStyle().equals(Style.FINAL))
                return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isCellEditable(Object cell) {
        String style = ((mxCell) cell).getStyle();
        if (style.equals(Style.INITIAL) || style.equals(Style.FINAL))
            return false;
        return true;
    }

    ;
    
    public boolean isReactToUpdateLabelEvent() {
		return reactToUpdateLabelEvent;
	}

	public void setReactToUpdateLabelEvent(boolean reactToUpdateLabelEvent) {
		this.reactToUpdateLabelEvent = reactToUpdateLabelEvent;
	}

	@Override
    public void cellLabelChanged(Object cell, Object value, boolean autoSize) {
        model.beginUpdate();
        try {
            getModel().setValue(cell, value);

            if (autoSize) {
                cellSizeUpdated(cell, false);
            }
        } finally {
            model.endUpdate();
        }
        fireEvent(new mxEventObject(mxEvent.LABEL_CHANGED, "cell", cell, "label", value));
    }
}
