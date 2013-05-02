package cn.dc.compiler;


import java.util.HashSet;
import java.util.Set;

public class AlphaMemoryNode extends AlphaNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Set<Object> readyObjects =new HashSet<Object>();
	
	private JoinNode joinNode;
	
	
	public Set<Object> getReadyObjects() {
		return readyObjects;
	}
	public void setReadyObjects(Set<Object> readyObjects) {
		this.readyObjects = readyObjects;
	}
	public JoinNode getJoinNode() {
		return joinNode;
	}
	public void setJoinNode(JoinNode joinNode) {
		this.joinNode = joinNode;
	}
	
	
}
