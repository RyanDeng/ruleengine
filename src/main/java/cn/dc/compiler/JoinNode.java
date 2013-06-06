package cn.dc.compiler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mvel2.MVEL;

import cn.dc.core.Rule;
import cn.dc.runtime.BuildReteTempData;

public class JoinNode implements Serializable, Node {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String expression;
	private BetaMemory betaMemory;
	private Node nextJoinOrRuleNode;
	private AlphaMemoryNode leftInputNode;
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

	public AlphaMemoryNode getLeftInputNode() {
		return leftInputNode;
	}

	public void setLeftInputNode(AlphaMemoryNode leftInputNode) {
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
	public void buildSelf(AlphaMemoryNode alphaMemoryNode,String leftVarString,BuildReteTempData reteTempData){
		this.leftInputNode=alphaMemoryNode;
		this.leftVariable=leftVarString;
		this.ruleName=alphaMemoryNode.getRuleName();
		List<String> varLists=reteTempData.getVariableList(expression, null);
		if(varLists.get(0).equals(leftVarString)){
			this.rightVariable=varLists.get(1);
		}else{
			this.rightVariable=varLists.get(0);
		}
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
			//右输入为joinNode
			}else if(rightInputNode instanceof JoinNode){
				JoinNode rightInputJoinNode=(JoinNode)rightInputNode;
				for(Map<String, Object> map:rightInputJoinNode.getBetaMemory().getReadyObjects()){
					HashMap<String,Object> mapAll=new HashMap<String, Object>();
					mapAll.putAll(map);
					mapAll.put(leftVariable, leftObj);
					if(eval(mapAll)){
						betaMemory.insert(mapAll);
					}
				}
			}		
		}
		if(nextJoinOrRuleNode instanceof RuleNode){
			return (RuleNode)nextJoinOrRuleNode;
		}else if(nextJoinOrRuleNode instanceof JoinNode){
			return ((JoinNode) nextJoinOrRuleNode).fireEval(this);
		}
		return null;
	}
	private boolean eval(Object leftobj,Object rightobj){
		Serializable compiled =MVEL.compileExpression(expression);
		Map vars = new HashMap();
		 vars.put(leftVariable, leftobj);
		 vars.put(rightVariable, rightobj);
		 Boolean res=(Boolean) MVEL.executeExpression(compiled,vars);
		 return res;
	}
	private boolean eval(Map map){
		Serializable compiled =MVEL.compileExpression(expression);
		Boolean res=(Boolean)MVEL.executeExpression(compiled,map);
		 return res;
	}
	public AlphaMemoryNode getOtherInputNode(Node oneSideNode){
		if(oneSideNode==leftInputNode){
			if(rightInputNode instanceof AlphaMemoryNode){
				return (AlphaMemoryNode)rightInputNode;
			}else{
				return null;
			}
		}else if(oneSideNode==rightInputNode){
			return (AlphaMemoryNode)leftInputNode;
		}
		return null;
	}

	public void dispose() {
		// TODO Auto-generated method stub
		betaMemory.getReadyObjects().clear();
	}
}
