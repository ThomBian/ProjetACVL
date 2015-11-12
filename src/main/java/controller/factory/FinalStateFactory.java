package controller.factory;

import model.FinalState;
import view.GraphView;

enum FinalStateFactory implements IStateFactory<FinalState> {

    INSTANCE {
        @Override
        public FinalState create(GraphView graph) {
            FinalState fs = new FinalState();
            graph.insertState(fs);
            return fs;
        }
    }

}
