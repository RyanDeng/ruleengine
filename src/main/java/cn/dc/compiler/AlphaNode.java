package cn.dc.compiler;

import java.io.Serializable;
import java.util.HashMap;

public class AlphaNode implements Serializable,Node{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String conditionValue;
	private HashMap<String, AlphaNode> nextNodes;
	private String ruleName;
	

	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
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
	public AlphaMemoryNode buildNextNodes(){
		nextNodes=nextNodes==null?new HashMap<String, AlphaNode>():nextNodes;
		AlphaMemoryNode node=null;
		if(!nextNodes.containsKey("")){
			node=new AlphaMemoryNode(this);
			nextNodes.put("", node);
		}
		return node;
	}
	
}
