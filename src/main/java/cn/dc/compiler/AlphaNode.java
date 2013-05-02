package cn.dc.compiler;

import java.io.Serializable;
import java.util.HashMap;

public class AlphaNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String conditionValue;
	private HashMap<String, AlphaNode> nextNodes;
	public String getConditionValue() {
		return conditionValue;
	}
	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}
	
}
