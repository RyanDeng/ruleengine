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
	
	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
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
		 Object res=MVEL.executeExpression(compiled,vars);
		 return Boolean.parseBoolean((String)res);
	}
	public List<AlphaMemoryNode> insert(Object obj) {
		Iterator it= nextNodes.entrySet().iterator();
		List<AlphaMemoryNode> alphaMemoryNodes=new ArrayList<AlphaMemoryNode>();
		while(it.hasNext()){
			Map.Entry entry=(Entry) it.next();
			AlphaNode alphaNode=(AlphaNode) entry.getValue();
			if(alphaNode instanceof AlphaMemoryNode){
				alphaMemoryNodes.add((AlphaMemoryNode)alphaNode);
			}else{
				if(alphaNode.eval(obj)){
					alphaNode.insert(obj);
				}
			}
		}
		return alphaMemoryNodes;
	}
}
