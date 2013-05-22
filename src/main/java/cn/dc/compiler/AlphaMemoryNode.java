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
	
	private List<AlphaNode> previousNodes;
	public AlphaMemoryNode(){
		this.joinNodes=new ArrayList<JoinNode>();
		this.previousNodes=new ArrayList<AlphaNode>();
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

	public AlphaNode getPreviousNode() {
		return previousNode;
	}

	public void setPreviousNode(AlphaNode previousNode) {
		this.previousNode = previousNode;
	}
	public void dispose(){
		for(JoinNode joinNode:joinNodes){
			joinNode.dispose();
		}
		readyObjects.clear();
	}
	
}
