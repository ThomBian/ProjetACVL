package controller.visitor;

import model.CompositeState;
import model.FinalState;
import model.InitialState;
import model.SimpleState;
import model.State;
import model.Transition;

public class FlattenVisitor implements Visitor {

	@Override
	public void visit(SimpleState s) {
		for(Transition<State> t : s.getOutgoingTransitions()){
			
		}
	}

	@Override
	public void visit(CompositeState c) {
		// Descendre
		for(State s : c.getStates()){
			if(s.isCompositeState()){
				s.apply(this);
			}
		}
		for(Transition<State> t : c.getOutgoingTransitions()){
			
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void visit(InitialState s) {
		
		Transition<InitialState> t = (Transition<InitialState>) s.getOutgoingTransitions().toArray()[0];
		State target = t.getDestination();
		target.apply(this);
		
		// ça remonte
	}

	@Override
	public void visit(FinalState s) {
		// TODO Auto-generated method stub

	}

}
