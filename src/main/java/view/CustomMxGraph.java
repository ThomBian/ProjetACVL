package view;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.view.mxGraph;

public class CustomMxGraph extends mxGraph {
	// Make all edges unmovable
	@Override
	public boolean isCellMovable(Object cell) {
		return !getModel().isEdge(cell);
	}
	// Disallow transition to be moved to another state 
	@Override 
	public boolean isCellDisconnectable(Object cell, Object terminal, boolean source){
		return false;
	}
	@Override
	public boolean isValidDropTarget(Object cell, Object[] cells) {
		try{
			if (cells.length == 1 && ((mxCell) cell).isVertex() && ((mxCell) cell).getStyle().equals(Style.COMPOSITE)) {
				return true;
			}
			return false;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean isValidConnection(Object source, Object target){
		// Disallow transition to an initial state
		if(((mxCell)target).getStyle().equals(Style.INITIAL)) return false;
		if(((mxCell)target).isEdge()) return false;
		return true;
	}
	
	// Disallow final state to have outgoing transition
	@Override
	public boolean isValidSource(Object cell){
		try{
			if(((mxCell) cell).getStyle().equals(Style.FINAL)) return false;
			return true;
		}catch(Exception e){
			return false;
		}
	}
	@Override
	public boolean isCellEditable(Object cell) {
		String style =  ((mxCell)cell).getStyle();
		if(style.equals(Style.INITIAL) || style.equals(Style.FINAL)) return false;
		return true;
	};
	@Override
	public void cellLabelChanged(Object cell, Object value, boolean autoSize)
	{
		model.beginUpdate();
		try
		{
			getModel().setValue(cell, value);

			if (autoSize)
			{
				cellSizeUpdated(cell, false);
			}
		}
		finally
		{
			model.endUpdate();
		}
		fireEvent(new mxEventObject(mxEvent.LABEL_CHANGED, "cell", cell,"label",value));
	}
}
