package view;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public class CustomMxGraph extends mxGraph{
	// Make all edges unmovable
	@Override
	  public boolean isCellMovable(Object cell)
	  {
	    return !getModel().isEdge(cell);
	  }	
	  public boolean isValidDropTarget(Object cell, Object[] cells){
		  // TODO : if cell  = null OK
		  if (cells.length == 1 && ((mxCell)cell).isVertex() && ((mxCell)cell).getStyle().equals(Style.COMPOSITE))
		  {	  
			  return true;
		  }
		  return false;
	  }
}
