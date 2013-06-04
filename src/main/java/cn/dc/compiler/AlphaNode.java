package cn.dc.compiler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mvel2.MVEL;

import cn.dc.core.Rule;

public class AlphaNode implements Serializable,Node{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String conditionValue;
	private HashMap<String, AlphaNode> nextNodes;
	private String ruleName;
	private String variable;
	private String objectTypeString;

	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public AlphaNode(){}
	public AlphaNode(String conditionValue,String objtypeString){
		this.conditionValue=conditionValue;
		this.objectTypeString=objtypeString;
	}
	public String getConditionValue() {
		return conditionValue;
	}
	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}
	
	public String getObjectTypeString() {
		return objectTypeString;
	}
	public void setObjectTypeString(String objectTypeString) {
		this.objectTypeString = objectTypeString;
	}
	
	public HashMap<String, AlphaNode> getNextNodes() {
		return nextNodes;
	}
	public AlphaNode buildNextNodes(AlphaNode nextNode){
		nextNodes=nextNodes==null?new HashMap<String, AlphaNode>():nextNodes;
		if(nextNode instanceof AlphaMemoryNode){
			if(!nextNodes.containsKey("")){
				nextNodes.put("", nextNode);
			}else{
				((AlphaMemoryNode)nextNodes.get("")).getJoinNodes().addAll(((AlphaMemoryNode) nextNode).getJoinNodes());
			}
			return null;
		}
		
		if(!nextNodes.containsKey(nextNode.getConditionValue())){
			nextNodes.put(nextNode.getConditionValue(), nextNode);
			return nextNode;
		}else {
			nextNodes.get(nextNode.getConditionValue()).setRuleName(nextNode.getRuleName());
			nextNodes.get(nextNode.getConditionValue()).setVariable(nextNode.getVariable());
			if(nextNode.getNextNodes()!=null){
				for(AlphaNode alphaNode:nextNode.getNextNodes().values()){
					if (alphaNode instanceof AlphaNode){
						nextNodes.get(nextNode.getConditionValue()).buildNextNodes(alphaNode);
					}
				}
			}
			return nextNodes.get(nextNode.getConditionValue());
		}
	}
	public void mergeFromAnoterAlphaNode(AlphaNode alphaNode){
		this.ruleName=alphaNode.getRuleName();
		this.variable=alphaNode.getVariable();
		for(AlphaNode nextNode: alphaNode.getNextNodes().values()){
			this.buildNextNodes(nextNode);
		}
	}
	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	public void buildself(String ruleName,String variableName, boolean isBuildMemory){
		this.setRuleName(ruleName);
		this.variable=variableName;
		if(isBuildMemory){
			AlphaMemoryNode alphaMemoryNode=buildNextNodes(ruleName);
		}
	}
	public void linkAlphaMemoryNode(AlphaMemoryNode alphaMemoryNode){
		nextNodes=nextNodes==null?new HashMap<String, AlphaNode>():nextNodes;
		if(!nextNodes.containsKey("")){
			nextNodes.put("", alphaMemoryNode);
		}else{
			nextNodes.get("").setRuleName(alphaMemoryNode.getRuleName());
		}
	}
	/**
	 * 没有条件，则直接连到AlphaMemoryNode
	 */
	private AlphaMemoryNode buildNextNodes(String ruleName){
		nextNodes=nextNodes==null?new HashMap<String, AlphaNode>():nextNodes;
		AlphaMemoryNode node=null;
		if(!nextNodes.containsKey("")){
			node=new AlphaMemoryNode(ruleName,null);
			nextNodes.put("", node);
			return node;
		}else{
			nextNodes.get("").setRuleName(ruleName);
			return (AlphaMemoryNode)nextNodes.get("");
		}
		
	}
	public List<Node> traverseAlphaNode(Rule rule){
		List<Node>  rightInputNodes=new ArrayList<Node>();
		Iterator it = nextNodes.entrySet().iterator();   
        while (it.hasNext()) {   
            Map.Entry entry = (Entry) it.next();  
            AlphaNode alphaNode=(AlphaNode)entry.getValue();
            if(alphaNode.getRuleName().equals(rule.getName())){
            	if(alphaNode instanceof AlphaMemoryNode){
            		rightInputNodes.add(alphaNode);
            	}else{
            		rightInputNodes.addAll(alphaNode.traverseAlphaNode(rule));
            	}
            }
        }
        return rightInputNodes;
	}
	public List<AlphaMemoryNode>  traverseAndFindAlphaMemoryNodes(Rule rule){
		List<AlphaMemoryNode> alphaMemoryNodes=new ArrayList<AlphaMemoryNode>();
		Iterator it = nextNodes.entrySet().iterator();   
        while (it.hasNext()) {   
            Map.Entry entry = (Entry) it.next();  
            AlphaNode alphaNode=(AlphaNode)entry.getValue();
            if(alphaNode.getRuleName().equals(rule.getName())){
            	if(alphaNode instanceof AlphaMemoryNode){
            		alphaMemoryNodes.add((AlphaMemoryNode)alphaNode);
            	}else{
            		alphaMemoryNodes.addAll(alphaNode.traverseAndFindAlphaMemoryNodes(rule));
            	}
            }
        }
        return alphaMemoryNodes;
	}
	public boolean eval(Object obj){
		if(conditionValue.equals("")){
			return true;
		}
		Serializable compiled =  MVEL.compileExpression(conditionValue);
		 Map vars = new HashMap();
		 vars.put(variable, obj);
		 Boolean res=(Boolean) MVEL.executeExpression(compiled,vars);
		 return res;
	}
	public List<AlphaMemoryNode> insert(Object obj) {
		Iterator it= nextNodes.entrySet().iterator();
		List<AlphaMemoryNode> alphaMemoryNodes=new ArrayList<AlphaMemoryNode>();
		while(it.hasNext()){
			Map.Entry entry=(Entry) it.next();
			AlphaNode alphaNode=(AlphaNode) entry.getValue();
			if(alphaNode instanceof AlphaMemoryNode){
				alphaMemoryNodes.add((AlphaMemoryNode)alphaNode);
				((AlphaMemoryNode) alphaNode).getReadyObjects().add(obj);
			}else{
				if(alphaNode.eval(obj)){
					alphaMemoryNodes.addAll(alphaNode.insert(obj));
				}
			}
		}
		return alphaMemoryNodes;
	}
	public void dispose() {
		for(Map.Entry entry:nextNodes.entrySet()){
			if(entry.getValue() instanceof AlphaNode){
				((AlphaNode)entry.getValue()).dispose();
			}else if(entry.getValue() instanceof AlphaMemoryNode){
				((AlphaMemoryNode)entry.getValue()).dispose();
			}
		}
		
	}
}
