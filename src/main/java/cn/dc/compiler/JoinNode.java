package cn.dc.compiler;

import java.io.Serializable;

import cn.dc.core.Rule;

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
	private String leftVariable;
	private String rightVariable;

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public JoinNode(){}
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
	public void buildRuleNode(Rule rule){
		nextJoinOrRuleNode=new RuleNode(rule, this);
	}

	public String getLeftVariable() {
		return leftVariable;
	}

	public void setLeftVariable(String leftVariable) {
		this.leftVariable = leftVariable;
	}

	public String getRightVariable() {
		return rightVariable;
	}

	public void setRightVariable(String rightVariable) {
		this.rightVariable = rightVariable;
	}
	
}
