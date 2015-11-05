package controller.visitor;

import model.CompositeState;
import model.FinalState;
import model.InitialState;
import model.SimpleState;

public interface Visitor {
	public void visit(SimpleState s);
	public void visit(CompositeState s);
	public void visit(InitialState s);
	public void visit(FinalState s);
}
