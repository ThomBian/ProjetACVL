package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class State { 
	public boolean isCompositeState(){
		return false;
	}

	public List<State> getAllStates() {
		List<State> sons = new ArrayList<State>();
		sons.add(this);
		return sons;
	}
}
