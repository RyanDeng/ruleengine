package cn.dc.compiler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	

	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	private HashMap<String, AlphaNode> alphaNodes;

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
	public HashSet<JoinNode>  buildAlphaNode(Column column,BuildReteTempData reteTempData,String ruleName){
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
			}else{
				AlphaNode alphaNode=new AlphaNode(condition.getExpression());
				alphaNode.setRuleName(ruleName);
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
					leftInput=alphaNodes.get(alphaNode.getConditionValue()).buildNextNodes();
				}
				previousAlphaNode=alphaNode;
			}
		}
		//若有joinCondition,则所有的这些condition都加上leftInput
		for(JoinCondition joinCondition:joinConditions){
			joinCondition.setLeftInputNode(leftInput);
		}
		//够在betanode
		return createBetaNode(joinConditions);
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
	private HashSet<JoinNode> createBetaNode(List<JoinCondition> joinConditions){
		HashSet<JoinNode> hs=new HashSet<JoinNode>();
		if(joinConditions!=null){
			for(JoinCondition joinCondition:joinConditions){
				JoinNode joinNode=new JoinNode(joinCondition.getCondition().getExpression());
				joinNode.setRuleName(ruleName);
				joinCondition.getLeftInputNode().setJoinNode(joinNode);
				joinNode.setLeftInputNode(joinCondition.getLeftInputNode());
				hs.add(joinNode);
			}
		}
		return hs;
	}
	//private HashSet<String> needToLinkAlphaMemNode(List<Condition> conditions){
//		HashSet<String> hs=new HashSet<String>();
//		for(int i=0;i<conditions.size();i++){
//			if(i+1<conditions.size() && conditions.get(i+1).getAndOr()==AndOr.OR){
//				hs.add(conditions.get(i).getExpression());
//			}
//		}
//	}
}
