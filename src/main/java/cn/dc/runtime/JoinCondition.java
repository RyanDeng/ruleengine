package cn.dc.runtime;

import cn.dc.compiler.AlphaMemoryNode;
import cn.dc.core.Condition;

public class JoinCondition {
	private Condition condition;
	private AlphaMemoryNode leftInputNode;
	public Condition getCondition() {
		return condition;
	}
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	public AlphaMemoryNode getLeftInputNode() {
		return leftInputNode;
	}
	public void setLeftInputNode(AlphaMemoryNode leftInputNode) {
		this.leftInputNode = leftInputNode;
	}
	
}
