package model;

public class Guard {
	
	private String condition;
	
	public Guard(String condition) {
		this.condition = condition;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "Guard{" +
			   "condition='" + condition + '\'' +
			   '}';
	}
}
