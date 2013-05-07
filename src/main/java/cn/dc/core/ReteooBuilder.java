package cn.dc.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import cn.dc.compiler.EntryPoint;
import cn.dc.compiler.JoinNode;
import cn.dc.compiler.ObjectType;
import cn.dc.compiler.ObjectTypeNode;
import cn.dc.runtime.BuildReteTempData;
import cn.dc.runtime.JoinCondition;

public class ReteooBuilder {
	private ReteooRuleBase ruleBase;
	private EntryPoint entryPoint;

	public ReteooBuilder(ReteooRuleBase ruleBase) {
		this.ruleBase = ruleBase;
		entryPoint = new EntryPoint();
	}

	public void buildReteoo(RulePackage rulePackage) {
		List<Rule> rules = rulePackage.getRules();
		for (Rule rule : rules) {
			createObjectType(rule, getBuildReteTempData(rule));
		
		}
	}

	private BuildReteTempData getBuildReteTempData(Rule rule) {
		List<String> ruleVariables = new ArrayList<String>();
		// List<String> objectTypeNames=new ArrayList<String>();
		for (Column column : rule.getColumns()) {
			ruleVariables.add(column.getName());
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

	private void createObjectType(Rule rule,
			BuildReteTempData reteTempData) {
		HashMap<String, ObjectTypeNode> objectTypeNodes = entryPoint
				.getObjectTypeNodes() == null ? new HashMap<String, ObjectTypeNode>()
				: entryPoint.getObjectTypeNodes();
		
		if (entryPoint.getObjectTypeNodes() == null) {
			entryPoint.setObjectTypeNodes(objectTypeNodes);
		}
		List<JoinNode> joinNodes=new ArrayList<JoinNode>();
		for (Column column : rule.getColumns()) {
			ObjectTypeNode objectTypeNode = new ObjectTypeNode(rule
					.getContainerPackage().getName());
			objectTypeNode
					.setObjectType(new ObjectType(column.getTypeAllpath()));
			objectTypeNode.setRuleName(rule.getName());
			//得到joinCondition
			joinNodes.addAll(objectTypeNode
					.buildAlphaNode(column, reteTempData,rule.getName()));
			if (!objectTypeNodes.containsKey(objectTypeNode.getObjectType()
					.getClassNameAllPath())) {
				objectTypeNodes.put(objectTypeNode.getObjectType()
						.getClassNameAllPath(), objectTypeNode);
			}
		}
		return ;
	}
	private void buildJoinNodes(List<JoinNode> joinNodes){
		if(joinNodes.size()==0){//rulenode上没有betanode则直接连接到alphanode
			
		}else{
			for(int i=0;i<joinNodes.size();i++){
				
			}
		}
	}
	private void findRightInput(JoinNode joinNode,)
	
}
