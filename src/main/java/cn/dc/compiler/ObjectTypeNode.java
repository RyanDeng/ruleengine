package cn.dc.compiler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mvel2.MVEL;
import org.mvel2.templates.res.EvalNode;

import cn.dc.core.Column;
import cn.dc.core.Condition;
import cn.dc.core.Rule;
import cn.dc.core.Condition.AndOr;
import cn.dc.runtime.BuildReteTempData;
import cn.dc.runtime.JoinCondition;

public class ObjectTypeNode implements Serializable,Node{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ObjectType objectType;
	private String pkgName;
	private String ruleName;
	private HashMap<String, AlphaNode> alphaNodes;

	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	
	public ObjectTypeNode(String pkgName){
		this.pkgName=pkgName;
	}
	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}
	
	public HashMap<String, AlphaNode> getAlphaNodes() {
		return alphaNodes;
	}

	public void setAlphaNodes(HashMap<String, AlphaNode> alphaNodes) {
		this.alphaNodes = alphaNodes;
	}

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	public List<JoinNode>  buildAlphaNode(Column column,BuildReteTempData reteTempData,String ruleName){
		alphaNodes= alphaNodes==null?new HashMap<String, AlphaNode>():alphaNodes;
		if(column.getConditions()==null){
			AlphaNode alphaNodeNull = new AlphaNode("");
			alphaNodeNull.setRuleName(ruleName);
			if(!alphaNodes.containsKey("")){
				alphaNodes.put("", alphaNodeNull);
				alphaNodeNull.buildNextNodes();
			}
			return null;
		}
		AlphaNode previousAlphaNode=null;
		int index=0;
		List<JoinCondition> joinConditions=new ArrayList<JoinCondition>();
		AlphaMemoryNode leftInput=null;
		//如果有joinCondition则所有的andOr都变成and
		makeConditionAndWhenJoinCondition(column,reteTempData);
		for(Condition condition:column.getConditions()){
			index++;
			if(reteTempData.isConditionJoin(condition.getExpression())){
				JoinCondition joinCondition=new JoinCondition();
				joinCondition.setCondition(condition);
				joinConditions.add(joinCondition);
				//column只有一个joinCondition就直接连接
				if(index==column.getConditions().size()){
					if(index==1){
						if(!alphaNodes.containsKey("")){
							AlphaNode alphaNode=new AlphaNode("");
							alphaNode.setRuleName(ruleName);
							alphaNodes.put("", alphaNode);
						}
						leftInput=alphaNodes.get("").buildNextNodes();
						leftInput.setRuleName(ruleName);
					}else{
						leftInput=previousAlphaNode.buildNextNodes();
						leftInput.setRuleName(ruleName);
					}
				}
			}else{
				AlphaNode alphaNode=new AlphaNode(condition.getExpression());
				alphaNode.setRuleName(ruleName);
				alphaNode.setVariable(reteTempData.getVariableList(condition.getExpression()).get(0));
				if(condition.getAndOr()==AndOr.AND){
					//之前有父alphaNode
					if(previousAlphaNode!=null){
						previousAlphaNode.buildNextNodes(alphaNode);
					}else{
						if(!alphaNodes.containsKey(alphaNode.getConditionValue())){
							alphaNodes.put(alphaNode.getConditionValue(), alphaNode);
						}
					}
				}else if (condition.getAndOr()==AndOr.OR) {
					//如果为or，那之前的condition要连接到memorynode
					if(previousAlphaNode!=null){
						previousAlphaNode.buildNextNodes();
					}
					if(!alphaNodes.containsKey(alphaNode.getConditionValue())){
						alphaNodes.put(alphaNode.getConditionValue(), alphaNode);
					}
				}
				//最后一个condition要连接到memorynode
				if(index==column.getConditions().size()){
					leftInput=alphaNodes.get(condition.getExpression()).buildNextNodes();
					leftInput.setRuleName(ruleName);
				}
				previousAlphaNode=alphaNode;
			}
			
		}
		//若有joinCondition,则所有的这些condition都加上leftInput
		for(JoinCondition joinCondition:joinConditions){
			joinCondition.setLeftInputNode(leftInput);
			String leftVar=reteTempData.getVariableList(leftInput.getPreviousNode().getConditionValue()).get(0);
		}
		//够在betanode
		return createBetaNode(joinConditions,reteTempData);
	}
	private void makeConditionAndWhenJoinCondition(Column column,BuildReteTempData reteTempData){
		boolean isJoinCondition=false;
		for(Condition condition:column.getConditions()){
			if(reteTempData.isConditionJoin(condition.getExpression())){
				isJoinCondition=true;
				break;
			}
		}
		for(Condition condition:column.getConditions()){
			condition.setAndOr("AND");
		}
	}
	private List<JoinNode> createBetaNode(List<JoinCondition> joinConditions, BuildReteTempData reteTempData){
		List<JoinNode> results=new ArrayList<JoinNode>();
		if(joinConditions!=null){
			for(JoinCondition joinCondition:joinConditions){
				JoinNode joinNode=new JoinNode(joinCondition.getCondition().getExpression());
				joinNode.setRuleName(ruleName);
				joinCondition.getLeftInputNode().setJoinNode(joinNode);
				joinNode.setLeftInputNode(joinCondition.getLeftInputNode());
				String leftVar=reteTempData.getVariableList(joinCondition.getLeftInputNode().getPreviousNode().getConditionValue()).get(0);
				joinNode.setLeftVariable(leftVar);
				results.add(joinNode);
			}
		}
		return results;
	}
	
	public List<AlphaMemoryNode> traverseAndFindAlphaMemoryNodes(Rule rule){
		List<AlphaMemoryNode> alphaMemoryNodes=new ArrayList<AlphaMemoryNode>();
		Iterator it= alphaNodes.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry entry=(Entry) it.next();
			AlphaNode alphaNode=(AlphaNode) entry.getValue();
			if(alphaNode.getRuleName().equals(rule.getName())){
				alphaMemoryNodes.addAll(alphaNode.traverseAndFindAlphaMemoryNodes(rule));
			}
		}
		return alphaMemoryNodes;
	}
	//private HashSet<String> needToLinkAlphaMemNode(List<Condition> conditions){
//		HashSet<String> hs=new HashSet<String>();
//		for(int i=0;i<conditions.size();i++){
//			if(i+1<conditions.size() && conditions.get(i+1).getAndOr()==AndOr.OR){
//				hs.add(conditions.get(i).getExpression());
//			}
//		}
//	}
	public List<AlphaMemoryNode> insert(Object obj) {
		List<AlphaMemoryNode> alphaMemoryNodes=new ArrayList<AlphaMemoryNode>();
		Iterator it= alphaNodes.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry entry=(Entry) it.next();
			AlphaNode alphaNode=(AlphaNode) entry.getValue();
			if(alphaNode.eval(obj)){
				alphaMemoryNodes.addAll(alphaNode.insert(obj));
			}
		}
		return alphaMemoryNodes;
		
	} 
	
}
