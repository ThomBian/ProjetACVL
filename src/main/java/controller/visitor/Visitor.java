package controller.visitor;

import model.FinalState;
import model.InitialState;
import model.NamedState;
import model.SimpleState;

public interface Visitor {
	public void visit(SimpleState s);
	public void visit(NamedState s);
	public void visit(InitialState s);
	public void visit(FinalState s);
}
