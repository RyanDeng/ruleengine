package cn.dc.compiler;

import java.util.List;
import java.util.Map;

import cn.dc.core.Rule;

public class RuleNode extends JoinNode implements Node {

	private String ruleName;
	private String thenStatements;
	private Node previousNode;
	private int salience;
	private boolean noLoop;
	
	//private List<AlphaMemoryNode> alphaMemoryNodes;
	
	public RuleNode(Rule rule){
		this.ruleName=rule.getName();
		this.thenStatements=rule.getThen();
		this.salience=rule.getSalience();
	}
	public RuleNode(Rule rule,Node previousNode){
		this(rule);
		this.previousNode=previousNode;
		
	}
//	public RuleNode(Rule rule,List<AlphaMemoryNode> alphaMemoryNodes){
//		this(rule);
//		this.alphaMemoryNodes=alphaMemoryNodes;
//	}
	public String getRuleName() {
		return ruleName;
	}
	public String getThenStatements() {
		return thenStatements;
	}
	public void setThenStatements(String thenStatements) {
		this.thenStatements = thenStatements;
	}
	public Node getPreviousNode() {
		return previousNode;
	}
	public void setPreviousNode(Node previousNode) {
		this.previousNode = previousNode;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public int getSalience() {
		return salience;
	}
//	public List<AlphaMemoryNode> getAlphaMemoryNodes() {
//		return alphaMemoryNodes;
//	}
	public void dispose(){
		
	}
	public boolean isNoLoop() {
		return noLoop;
	}
	
}
