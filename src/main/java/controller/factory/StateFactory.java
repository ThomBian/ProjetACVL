package controller.factory;

import model.CompositeState;
import model.FinalState;
import model.InitialState;
import model.SimpleState;
import model.State;
import view.GraphView;

/**
 * <p>
 * {@link IFactory} abstract for states. Acts as a facade for all implementation
 * to secure access from other packages.
 * 
 * @author ncouret
 *
 * @param <T>
 */
public abstract class StateFactory<T extends State> implements IFactory<T> {

	protected StateFactory() {
	}

	@Override
	public abstract T create(GraphView graph);

	public static FinalState createFinalState(GraphView graph) {
		return FinalStateFactory.getInstance().create(graph);
	}

	public static InitialState createInitialState(GraphView graph) {
		return InitialStateFactory.getInstance().create(graph);
	}

	public static SimpleState createSimpleState(GraphView graph, String name) {
		return SimpleStateFactory.getInstance().create(graph, name);
	}

	public static CompositeState createCompositeState(GraphView graph, String name) {
		return CompositeStateFactory.getInstance().create(graph, name);
	}

}
