package model;

import controller.Diagram;
import controller.DiagramError;
public class FinalState extends State {

	public String toString(){
		return "[Final State]";
	}

    @Override
    public void setReach(boolean reach) {
        this.reach = reach;
    }


}
