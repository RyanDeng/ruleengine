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
	public AlphaNode(){}
	public AlphaNode(String conditionValue){
		this.conditionValue=conditionValue;
	}
	public String getConditionValue() {
		return conditionValue;
	}
	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}
	public void buildNextNodes(AlphaNode nextNode){
		nextNodes=nextNodes==null?new HashMap<String, AlphaNode>():nextNodes;
		if(!nextNodes.containsKey(nextNode.getConditionValue())){
			nextNodes.put(nextNode.getConditionValue(), nextNode);
		}
	}
	/**
	 * 没有条件，则直接连到AlphaMemoryNode
	 */
	public void buildNextNodes(){
		nextNodes=nextNodes==null?new HashMap<String, AlphaNode>():nextNodes;
		if(!nextNodes.containsKey("")){
			nextNodes.put("", new AlphaMemoryNode(this));
		}
	}
	
}
