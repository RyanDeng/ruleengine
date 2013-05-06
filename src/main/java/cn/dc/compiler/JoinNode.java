package cn.dc.compiler;

import java.io.Serializable;

public class JoinNode implements Serializable, Node {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String expression;
	private BetaMemory betaMemory;
	private Node nextJoinOrRuleNode;
	private Node leftInputNode;
	private Node rightInputNode;
	private String ruleName;

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public JoinNode(String expr) {
		this.expression = expr;
		betaMemory = new BetaMemory();
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public BetaMemory getBetaMemory() {
		return betaMemory;
	}

	public void setBetaMemory(BetaMemory betaMemory) {
		this.betaMemory = betaMemory;
	}

	public Node getNextJoinOrRuleNode() {
		return nextJoinOrRuleNode;
	}

	public void setNextJoinOrRuleNode(Node nextJoinOrRuleNode) {
		this.nextJoinOrRuleNode = nextJoinOrRuleNode;
	}

	public Node getLeftInputNode() {
		return leftInputNode;
	}

	public void setLeftInputNode(Node leftInputNode) {
		this.leftInputNode = leftInputNode;
	}

	public Node getRightInputNode() {
		return rightInputNode;
	}

	public void setRightInputNode(Node rightInputNode) {
		this.rightInputNode = rightInputNode;
	}

}
