package view;

import com.mxgraph.model.mxCell;
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
		// TODO : if cell = null
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
		if(source.equals(target)) return true;
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
}
