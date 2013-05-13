package cn.dc.compiler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

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
	public RuleNode fireEval(Node node){
		//判断是否左右两个输入都是有数据的
		if(node==leftInputNode){
			if(rightInputNode instanceof AlphaMemoryNode){
				AlphaMemoryNode rightInputAlphaMemoryNode=(AlphaMemoryNode)rightInputNode;
				if(rightInputAlphaMemoryNode.getReadyObjects().size()==0){
					return null;
				}
			}else{
				return null;
			}
		}else if(node==rightInputNode){
			AlphaMemoryNode leftInputAlphaMemoryNode=(AlphaMemoryNode)leftInputNode;
			if(leftInputAlphaMemoryNode.getReadyObjects().size()==0){
				return null;
			}
		}
		
		
		AlphaMemoryNode leftInputAlphaMemoryNode=(AlphaMemoryNode)leftInputNode;
		for(Object leftObj:leftInputAlphaMemoryNode.getReadyObjects()){
			//右输入为alphaMemoryNode
			if(rightInputNode instanceof AlphaMemoryNode){
				AlphaMemoryNode rightInputAlphaMemoryNode=(AlphaMemoryNode)rightInputNode;
				for(Object rightObj:rightInputAlphaMemoryNode.getReadyObjects()){
					if(eval(leftObj, rightObj)){
						HashMap<String, Object> map=new HashMap<String, Object>();
						map.put(leftVariable, leftObj);
						map.put(rightVariable, rightObj);
						betaMemory.insert(map);
					}
				}
				if(nextJoinOrRuleNode instanceof JoinNode){
					return ((JoinNode) nextJoinOrRuleNode).fireEval(this);
				}else{
					return ()nextJoinOrRuleNode;
				}
			}else if(rightInputNode instanceof JoinNode){
				JoinNode rightInputJoinNode=(JoinNode)rightInputNode;
				
			}
		}
	}
	private boolean eval(Object leftobj,Object rightobj){
		Serializable compiled =MVEL.compileExpression(expression);
		Map vars = new HashMap();
		 vars.put(leftVariable, leftobj);
		 vars.put(rightInputNode, rightobj);
		 Object res=MVEL.executeExpression(compiled,vars);
		 return Boolean.parseBoolean((String)res);
	}
	private boolean eval(Map map){
		Serializable compiled =MVEL.compileExpression(expression);
		Object res=MVEL.executeExpression(compiled,map);
		 return Boolean.parseBoolean((String)res);
	}
	public AlphaMemoryNode getOtherInputNode(AlphaMemoryNode alphaMemoryNode){
		if(alphaMemoryNode==leftInputNode){
			if(rightInputNode instanceof AlphaMemoryNode){
				return (AlphaMemoryNode)rightInputNode;
			}else{
				return null;
			}
		}else if(alphaMemoryNode==rightInputNode){
			return (AlphaMemoryNode)leftInputNode;
		}
		return null;
	}
}
