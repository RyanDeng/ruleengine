package cn.dc.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.dc.compiler.AlphaMemoryNode;
import cn.dc.compiler.AlphaNode;
import cn.dc.compiler.EntryPoint;
import cn.dc.compiler.JoinNode;
import cn.dc.compiler.Node;
import cn.dc.compiler.ObjectType;
import cn.dc.compiler.ObjectTypeNode;
import cn.dc.compiler.RuleNode;
import cn.dc.runtime.BuildReteTempData;

public class ReteooBuilder {
	private ReteooRuleBase ruleBase;
	private EntryPoint entryPoint;

	public ReteooBuilder(ReteooRuleBase ruleBase) {
		this.ruleBase = ruleBase;
		entryPoint = new EntryPoint();
	}

	public EntryPoint getEntryPoint() {
		return entryPoint;
	}

	public void buildReteoo(RulePackage rulePackage) {
		List<Rule> rules = rulePackage.getRules();
		for (Rule rule : rules) {
			buildObjectTypeNode(rule, getBuildReteTempData(rule));
 			System.out.println("test");
		}
	}

	private BuildReteTempData getBuildReteTempData(Rule rule) {
		Map<String, String> ruleVariables = new HashMap<String, String>();
		// List<String> objectTypeNames=new ArrayList<String>();
		for (Column column : rule.getColumns()) {
			ruleVariables.put(column.getTypeAllpath(), column.getName());
			// objectTypeNames.add(column.getTypeAllpath());
		}
		return new BuildReteTempData(ruleVariables);
	}

	private List<String> analyseObjectType(Rule rule) {
		List<String> objectTypeNames = new ArrayList<String>();
		for (Column column : rule.getColumns()) {
			objectTypeNames.add(column.getTypeAllpath());
		}
		return objectTypeNames;
	}
	//改写
	private void buildObjectTypeNode(Rule rule,BuildReteTempData reteTempData){
		HashMap<String, ObjectTypeNode> objectTypeNodes = entryPoint
				.getObjectTypeNodes() == null ? new HashMap<String, ObjectTypeNode>()
				: entryPoint.getObjectTypeNodes();

		if (entryPoint.getObjectTypeNodes() == null) {
			entryPoint.setObjectTypeNodes(objectTypeNodes);
		}
		List<List<AlphaNode>> everyTailNodesList=new ArrayList<List<AlphaNode>>();
		for(Column column : rule.getColumns()){
			ObjectTypeNode objectTypeNode = new ObjectTypeNode(rule
									.getContainerPackage().getName());
			List<AlphaNode> tailNodes;
			tailNodes=objectTypeNode.buildself(rule, column, getBuildReteTempData(rule));
			if (!objectTypeNodes.containsKey(objectTypeNode.getObjectType()
					.getClassNameAllPath())) {
				objectTypeNodes.put(objectTypeNode.getObjectType()
						.getClassNameAllPath(), objectTypeNode);
			}else{
				ObjectTypeNode existedObjectTypeNode=objectTypeNodes.get(objectTypeNode.getObjectType()
					.getClassNameAllPath());
				//要写
				existedObjectTypeNode.mergeFromAnotherObjectTypeNode(objectTypeNode);
			}
			
			everyTailNodesList.add(tailNodes);
		}
		Node lastNode=linkJoinNode(everyTailNodesList, rule.getName());
		linkRuleNode(lastNode, rule);
		
	}
	private void linkRuleNode(Node lastNode,Rule rule){
		RuleNode ruleNode=new RuleNode(rule, lastNode);
		if(lastNode instanceof AlphaMemoryNode){
			ruleNode.setVarString(((AlphaMemoryNode) lastNode).getVarString());
			((AlphaMemoryNode) lastNode).getJoinNodes().add(ruleNode);
		}else if(lastNode instanceof JoinNode){
			((JoinNode) lastNode).setNextJoinOrRuleNode(ruleNode);
		}
	}
	private Node linkJoinNode(List<List<AlphaNode>> everytailNodes,String rulename){
		Map<String, Node> everyTailNodeMap=new LinkedHashMap<String, Node>();
		Node lastJoinNode=null;
		for(List<AlphaNode> tailAlphaNodes:everytailNodes){
			AlphaNode oneOfTailNode=tailAlphaNodes.get(0);
			List<JoinNode> joinNodes=((AlphaMemoryNode)oneOfTailNode.getNextNodes().get("")).getJoinNodes();
			int index=-2;
			if((index=findHasJoinNode(rulename, joinNodes))>=0){
				everyTailNodeMap.put(tailAlphaNodes.get(0).getVariable(), joinNodes.get(index));
				lastJoinNode=joinNodes.get(index);
			}else{
				everyTailNodeMap.put(tailAlphaNodes.get(0).getVariable(), (AlphaMemoryNode)oneOfTailNode.getNextNodes().get(""));
				lastJoinNode=(AlphaMemoryNode)oneOfTailNode.getNextNodes().get("");
			}
		}
		for(Map.Entry<String, Node> entry: everyTailNodeMap.entrySet()){
			if(entry.getValue() instanceof JoinNode){
				JoinNode joinNode=(JoinNode) entry.getValue();
				Node inputNode=everyTailNodeMap.get(joinNode.getRightVariable());
				joinNode.setRightInputNode(inputNode);
				if(inputNode instanceof JoinNode){
					((JoinNode) inputNode).setNextJoinOrRuleNode(joinNode);
				}else if(inputNode instanceof AlphaMemoryNode){
					((AlphaMemoryNode) inputNode).getJoinNodes().add(joinNode);
				}
				
			}
		}
		return lastJoinNode;
	}
	private int findHasJoinNode(String ruleName,List<JoinNode> joinNodes){
		for(int i=0;i<joinNodes.size();i++){
			if(joinNodes.get(i).getRuleName().equals(ruleName)){
				return i;
			}
		}
		return -1;
	}
//	/**
//	 * 生成objectType,并包括下面的alphaNode
//	 * 
//	 * @param rule
//	 * @param reteTempData
//	 * 被改写
//	 */
//	private void createObjectType(Rule rule, BuildReteTempData reteTempData) {
//		HashMap<String, ObjectTypeNode> objectTypeNodes = entryPoint
//				.getObjectTypeNodes() == null ? new HashMap<String, ObjectTypeNode>()
//				: entryPoint.getObjectTypeNodes();
//
//		if (entryPoint.getObjectTypeNodes() == null) {
//			entryPoint.setObjectTypeNodes(objectTypeNodes);
//		}
//		List<JoinNode> joinNodes = new ArrayList<JoinNode>();
//		for (Column column : rule.getColumns()) {
//			ObjectTypeNode objectTypeNode = new ObjectTypeNode(rule
//					.getContainerPackage().getName());
//			objectTypeNode
//					.setObjectType(new ObjectType(column.getTypeAllpath()));
//			objectTypeNode.setRuleName(rule.getName());
//			// 得到joinCondition
//			joinNodes.addAll(objectTypeNode.buildAlphaNode(column,
//					reteTempData, rule.getName()));
//			if (!objectTypeNodes.containsKey(objectTypeNode.getObjectType()
//					.getClassNameAllPath())) {
//				objectTypeNodes.put(objectTypeNode.getObjectType()
//						.getClassNameAllPath(), objectTypeNode);
//			}
//		}
//
//		buildJoinNodes(joinNodes, rule, reteTempData);
//		return;
//	}

//	/**
//	 * 为每个joinode链接左右两输入
//	 * 
//	 * @param joinNodes
//	 * @param reteTempData
//	 */
//	private void buildJoinNodes(List<JoinNode> joinNodes, Rule rule,
//			BuildReteTempData reteTempData) {
//		if (joinNodes.size() == 0) {// rulenode上没有betanode则直接连接到alphaMemoryNode
//			List<AlphaMemoryNode> alphaMemoryNodes = traverseAndFindAlphaMemoryNodes(rule);
//			RuleNode ruleNode = new RuleNode(rule, alphaMemoryNodes);
//			for (AlphaMemoryNode alphaMemoryNode : alphaMemoryNodes) {
//				alphaMemoryNode.setJoinNode(ruleNode);
//			}
//		} else {
//			int level = 1;
//			JoinNode previousJoinNode = null;
//			for (JoinNode joinNode : joinNodes) {
//				if (level == 1) {
//					if (previousJoinNode == null) {
//						ObjectTypeNode rightInputNode = findRightInput(
//								joinNode, rule);
//						List<Node> rightInputMemorynodes = traverseObjectTypeNode(
//								rule, rightInputNode);
//						for (Node node : rightInputMemorynodes) {
//							AlphaMemoryNode alphaMemoryNode = (AlphaMemoryNode) node;
//							alphaMemoryNode.setJoinNode(joinNode);
//							joinNode.setRightInputNode(alphaMemoryNode);
//							String rightVar = reteTempData.getVariableList(
//									alphaMemoryNode.getPreviousNode()
//											.getConditionValue(),
//									alphaMemoryNode.getPreviousNode()
//											.getObjectTypeString()).get(0);
//							joinNode.setRightVariable(rightVar);
//						}
//
//					}
//				} else {
//					joinNode.setRightInputNode(previousJoinNode);
//					previousJoinNode.setNextJoinOrRuleNode(joinNode);
//					String rightVar = previousJoinNode.getLeftVariable();
//					joinNode.setRightVariable(rightVar);
//				}
//				previousJoinNode = joinNode;
//				level++;
//			}
//			joinNodes.get(joinNodes.size() - 1).buildRuleNode(rule);
//		}
//	}

//	/**
//	 * 根据joinode，找到右输入ObjectTypeNode
//	 * 
//	 * @param joinNode
//	 * @param rule
//	 * @return
//	 */
//	private ObjectTypeNode findRightInput(JoinNode joinNode, Rule rule) {
//		AlphaMemoryNode alphaMemoryNode = (AlphaMemoryNode) joinNode
//				.getLeftInputNode();
//		BuildReteTempData buildReteTempData = getBuildReteTempData(rule);
//		String rightInputVarString = null;
//		List<String> inputsVar = buildReteTempData.getVariableList(
//				joinNode.getExpression(), null);
//		// 判断返回的两个参数名字，左输入含有则另一个是右输入
//		if (buildReteTempData
//				.getVariableList(
//						alphaMemoryNode.getPreviousNode().getConditionValue(),
//						alphaMemoryNode.getPreviousNode().getObjectTypeString())
//				.get(0).equals(inputsVar.get(0))) {
//			rightInputVarString = inputsVar.get(1);
//		} else {
//			rightInputVarString = inputsVar.get(0);
//		}
//		ObjectTypeNode rightObjectTypeNode = null;
//		for (Column column : rule.getColumns()) {
//			if (rightInputVarString.equals(column.getName())) {
//				rightObjectTypeNode = entryPoint.getObjectTypeNodes().get(
//						column.getTypeAllpath());
//			}
//		}
//		return rightObjectTypeNode;
//	}

	/**
	 * 根据ObjectTypeNode得到最底层的alphamemorynode
	 * 
	 * @param rule
	 * @param objectTypeNode
	 * @return
	 */
	private List<Node> traverseObjectTypeNode(Rule rule,
			ObjectTypeNode objectTypeNode) {
		List<Node> rightInputNodes = new ArrayList<Node>();
		Iterator it = objectTypeNode.getAlphaNodes().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Entry) it.next();
			AlphaNode alphaNode = (AlphaNode) entry.getValue();
			if (alphaNode.getRuleName().equals(rule.getName())) {
				rightInputNodes.addAll(alphaNode.traverseAlphaNode(rule));
			}
		}
		return rightInputNodes;

	}

	private List<AlphaMemoryNode> traverseAndFindAlphaMemoryNodes(Rule rule) {
		Iterator it = entryPoint.getObjectTypeNodes().entrySet().iterator();
		List<AlphaMemoryNode> alphaMemoryNodes = new ArrayList<AlphaMemoryNode>();
		while (it.hasNext()) {
			Map.Entry entry = (Entry) it.next();
			ObjectTypeNode objectTypeNode = (ObjectTypeNode) entry.getValue();
			if (objectTypeNode.getRuleName().equals(rule.getName())) {
				alphaMemoryNodes.addAll(objectTypeNode
						.traverseAndFindAlphaMemoryNodes(rule));
			}
		}
		return alphaMemoryNodes;
	}

}
