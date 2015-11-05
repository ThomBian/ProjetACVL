package controller.factory;

import model.State;
import model.Transition;
import view.GraphView;

public abstract class TransitionFactory<T extends Transition<State>> implements IFactory<T> {

	protected TransitionFactory() {
	}

	@Override
	public abstract T create(GraphView graph);

}
