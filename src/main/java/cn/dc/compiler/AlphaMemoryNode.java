package cn.dc.compiler;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlphaMemoryNode extends AlphaNode implements Node{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Set<Object> readyObjects =new HashSet<Object>();
	
	private List<JoinNode> joinNodes;
	
	private String ruleName;
	private String varString;
	
//	private List<AlphaNode> previousNodes;
	public AlphaMemoryNode(String ruleName,String varString){
		this.joinNodes=new ArrayList<JoinNode>();
		this.ruleName=ruleName;
		this.varString=varString;
		//this.previousNodes=new ArrayList<AlphaNode>();
	}
	public void buildSelf(String ruleName,String varString){
		this.ruleName=ruleName;
		this.varString=varString;
	}
	public Set<Object> getReadyObjects() {
		return readyObjects;
	}
	public void setReadyObjects(Set<Object> readyObjects) {
		this.readyObjects = readyObjects;
	}
	public List<JoinNode> getJoinNodes() {
		return joinNodes;
	}
	public void setJoinNodes(List<JoinNode> joinNodes) {
		this.joinNodes = joinNodes;
	}
	
	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getVarString() {
		return varString;
	}

	public void setVarString(String varString) {
		this.varString = varString;
	}

	public void linkJoinNode(JoinNode joinNode){
		joinNodes.add(joinNode);
	}
//	public AlphaNode getPreviousNode() {
//		return previousNode;
//	}
//
//	public void setPreviousNode(AlphaNode previousNode) {
//		this.previousNode = previousNode;
//	}
	public void dispose(){
		for(JoinNode joinNode:joinNodes){
			joinNode.dispose();
		}
		readyObjects.clear();
	}
	
}
