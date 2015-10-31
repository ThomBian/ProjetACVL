package model;

import java.util.Set;

public class InitialState extends State {
	

	public String toString(){
		return "[Initial State]";
	}
	public boolean isInitialState() {
		return true;
	}
}
