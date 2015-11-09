package controller.factory;

import model.CompositeState;
import model.FinalState;
import model.InitialState;
import model.SimpleState;
import model.State;
import view.GraphView;

/**
 * <p>
 * Factory facade for States. Provide convenience methods to create States.
 * 
 * @author ncouret
 *
 */
public final class StateFactory<T extends State> {

	protected StateFactory() {
	}

	public static FinalState createFinalState(GraphView graph) {
		return FinalStateFactory.INSTANCE.create(graph);
	}

	public static InitialState createInitialState(GraphView graph) {
		return InitialStateFactory.INSTANCE.create(graph);
	}

	public static SimpleState createSimpleState(GraphView graph, String name) {
		return SimpleStateFactory.getInstance().create(graph, name);
	}

	public static CompositeState createCompositeState(GraphView graph, String name) {
		return CompositeStateFactory.getInstance().create(graph, name);
	}

}
