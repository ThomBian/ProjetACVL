package model;

import java.util.Set;

public abstract class NamedState extends State {

	private String name;


	public NamedState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
