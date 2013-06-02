package cn.dc.compiler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.dc.core.Column;
import cn.dc.core.Condition;
import cn.dc.core.Rule;
import cn.dc.core.Condition.AndOr;
import cn.dc.runtime.BuildReteTempData;
import cn.dc.runtime.JoinCondition;

public class ObjectTypeNode implements Serializable, Node {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ObjectType objectType;
	private String pkgName;//rule的包名暂定需不需要
	private String ruleName;
	private HashMap<String, AlphaNode> alphaNodes;

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public ObjectTypeNode(String pkgName) {
		this.pkgName = pkgName;
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
	private List<AlphaNode> buildAlphaNode(Column column, BuildReteTempData reteTempData){
		alphaNodes = alphaNodes == null ? new HashMap<String, AlphaNode>()
				: alphaNodes;
		String variableNameString=reteTempData.getVariables().get(this.objectType.getClassNameAllPath());
		int lastIndexOfSingleCondition=figuareIndexOfAddMemory(column.getConditions(), reteTempData);
		if(column.getConditions()==null || lastIndexOfSingleCondition==-1){
			AlphaNode alphaNodeNull=new AlphaNode("", column.getTypeAllpath());
			alphaNodeNull.buildself(ruleName,null,true);
			if (!alphaNodes.containsKey("")) {
				alphaNodes.put("", alphaNodeNull);
			}else{
				AlphaMemoryNode alphaMemoryNode=(AlphaMemoryNode) alphaNodes.get("").getNextNodes().get("");
				alphaMemoryNode.buildSelf(ruleName, null);
			}
			List<AlphaNode> results=new ArrayList<AlphaNode>();
			results.add(alphaNodes.get(""));
			if(lastIndexOfSingleCondition!=-1)
				return results;
		}
		if(lastIndexOfSingleCondition!=-1){//如果非单条的joinode
			Map<String, List<AlphaNode>> results=buildAlphaNetwork(column.getConditions(), 0, lastIndexOfSingleCondition, variableNameString);
		
			for(AlphaNode headNode: results.get("head")){
				addAlphaNodeToAlphaNodes(headNode);
			}
		
			AlphaMemoryNode alphaMemoryNode=new AlphaMemoryNode(this.ruleName,variableNameString);
			for(AlphaNode tailNode:results.get("tail")){
				tailNode.linkAlphaMemoryNode(alphaMemoryNode);
			}
		
			//连接
			if(lastIndexOfSingleCondition!=column.getConditions().size()-1){
				JoinNode joinNode=buildJoinNode(column.getConditions().get(lastIndexOfSingleCondition+1));
				joinNode.buildSelf(alphaMemoryNode, variableNameString,reteTempData);
				for(AlphaNode tailNode:results.get("tail")){
					AlphaMemoryNode tailAlphaMemoryNode=(AlphaMemoryNode) tailNode.getNextNodes().get("");
					tailAlphaMemoryNode.linkJoinNode(joinNode);
				}
			}
			return results.get("tail");
		}else{
			AlphaMemoryNode alphaLinkMemoryNode=(AlphaMemoryNode) alphaNodes.get("").getNextNodes().get("");
			JoinNode joinNode=buildJoinNode(column.getConditions().get(lastIndexOfSingleCondition+1));
			joinNode.buildSelf(alphaLinkMemoryNode, variableNameString,reteTempData);
			alphaLinkMemoryNode.linkJoinNode(joinNode);
			List<AlphaNode> results=new ArrayList<AlphaNode>();
			results.add(alphaNodes.get(""));
			return results;
		}
		
		
	}
	public void mergeFromAnotherObjectTypeNode(ObjectTypeNode objectTypeNode){
		this.setRuleName(ruleName);
		//yaoxie 
		for(Map.Entry<String,AlphaNode> entry : objectTypeNode.getAlphaNodes().entrySet()){
			if (!alphaNodes.containsKey(entry.getKey())) {
				alphaNodes.put(entry.getKey(), entry.getValue());
			}else{
//				if(entry.getValue() instanceof AlphaMemoryNode){
//					if (!alphaNodes.containsKey("")) {
//						alphaNodes.put("", entry.getValue());
//					}else{
//						((AlphaMemoryNode)alphaNodes.get("")).getJoinNodes().addAll(((AlphaMemoryNode)entry.getValue()).getJoinNodes());
//					}
//				}
				alphaNodes.get(entry.getKey()).mergeFromAnoterAlphaNode(entry.getValue());
				
			}
		}
	}
	public List<AlphaNode> buildself(Rule rule,Column column,BuildReteTempData buildReteTempData){
		this.objectType=new ObjectType(column.getTypeAllpath());
		this.setRuleName(rule.getName());
		return this.buildAlphaNode(column, buildReteTempData);
	}
	private JoinNode buildJoinNode(Condition condition){
		JoinNode joinNode=new JoinNode(condition.getExpression());
		return joinNode;
	}
	private Map<String, List<AlphaNode>> buildAlphaNetwork(List<Condition> conditions ,int start,int end,String variableNameString){
		Map<String, List<AlphaNode>> results=new HashMap<String, List<AlphaNode>>();
		results.put("head", new ArrayList<AlphaNode>());
		results.put("tail", new ArrayList<AlphaNode>());
		List<AlphaNode> previousAlphaNodes = new ArrayList<AlphaNode>();//为了or关系
		//List<Node> fathers=new ArrayList<Node>();//为了or关系能够找到父节点进行连接
		List<AlphaNode> tailNodes=new ArrayList<AlphaNode>();
		for(int i=start;i<=end;i++){
			Condition condition=conditions.get(i);	
			AlphaNode alphaNode = new AlphaNode(condition.getExpression(),
					this.objectType.getClassNameAllPath());
			alphaNode.buildself(ruleName, variableNameString, false);
			
			boolean isTheFirst= i==0 || i==start ;
			if( isTheFirst || condition.getAndOr() == AndOr.AND ){
				if(isTheFirst){
					results.get("head").add(alphaNode);
				}
				
				if(!isTheFirst && condition.getBracket()!=null && condition.getBracket().equals("left")){
					int rightIndex=findNextRightBracketIndex(i, conditions);
					Map<String, List<AlphaNode>> middleResults=buildAlphaNetwork(conditions, i, rightIndex, variableNameString);
					for(AlphaNode previousAlphaNode:previousAlphaNodes){
						for(AlphaNode headNode:middleResults.get("head")){
							previousAlphaNode.buildNextNodes(headNode);
						}
					}
					previousAlphaNodes.clear();
					previousAlphaNodes.addAll(middleResults.get("tail"));
					tailNodes.clear();
					tailNodes.addAll(middleResults.get("tail"));
					i=rightIndex;
				}else{
					AlphaNode linkNode=null;
					for (AlphaNode previousAlphaNode:previousAlphaNodes) {
						linkNode=previousAlphaNode.buildNextNodes(alphaNode);
					}
					previousAlphaNodes.clear();
					tailNodes.clear();
					if(linkNode==null){
						previousAlphaNodes.add(alphaNode);
						tailNodes.add(alphaNode);
					}else{
						previousAlphaNodes.add(linkNode);
						tailNodes.add(linkNode);
					}
					
				}
			}else if(condition.getAndOr() == AndOr.OR){
				if(condition.getBracket()!=null && condition.getBracket().equals("left")){
					int rightIndex=findNextRightBracketIndex(i, conditions);
					Map<String, List<AlphaNode>> middleResults=buildAlphaNetwork(conditions, i, rightIndex, variableNameString);
					results.get("head").addAll(middleResults.get("head"));
					previousAlphaNodes.addAll(middleResults.get("tail"));
					tailNodes.addAll(middleResults.get("tail"));
					i=rightIndex;
				}else{
					results.get("head").add(alphaNode);
					previousAlphaNodes.add(alphaNode);
					tailNodes.add(alphaNode);
				}
			}
		}
		results.get("tail").addAll(tailNodes);
		return results;
//			if(i==start){
//				addAlphaNodeToAlphaNodes(alphaNode);
//				if(i+1<=end && conditions.get(i+1).getAndOr()==AndOr.OR){
//					}//预测下一个是否为or是的话把father加上去}
//				previousAlphaNodes.add(alphaNode);
//			}else {
//				if (condition.getAndOr() == AndOr.AND) {
//					// 之前有父alphaNode
//					for (AlphaNode previousAlphaNode:previousAlphaNodes) {
//						previousAlphaNode.buildNextNodes(alphaNode);
//					}
//					if(i+1<=lastIndexOfSingleCondition && column.getConditions().get(i+1).getAndOr()==AndOr.OR){
//						}//预测下一个是否为or是的话把father加上去}
//					previousAlphaNodes.clear();
//					previousAlphaNodes.add(alphaNode);
//
//				} else if (condition.getAndOr() == AndOr.OR) {
//					if(fatherNode!=null){
//						if(fatherNode instanceof ObjectTypeNode){
//							addAlphaNodeToAlphaNodes(alphaNode);
//						}else if(fatherNode instanceof AlphaNode){
//							((AlphaNode) fatherNode).buildNextNodes(alphaNode);
//						}	
//						previousAlphaNodes.add(alphaNode);
//					}
//					
//					if (!alphaNodes.containsKey(alphaNode.getConditionValue())) {
//						alphaNodes
//								.put(alphaNode.getConditionValue(), alphaNode);
//					}
//				}
//			}
	}
	/**
	 * 根据左括号找到右括号的index
	 * @param leftIndex
	 * @param conditions
	 * @return
	 */
	private int findNextRightBracketIndex(int leftIndex, List<Condition> conditions){
		int leftCount=0;
		for(int i=leftIndex+1;i<conditions.size();i++){
			if(conditions.get(i).getBracket()==null) break;
			if(conditions.get(i).getBracket().equals("left")){
				leftCount++;
			}else if (conditions.get(i).getBracket().equals("right")) {
				if(leftCount>0){
					leftCount--;
				}else{
					return i;
				}
			}
		}
		return -1;
	}
	/**
	 * 计算第几个condition后要加alphamemorynode
	 * @return
	 */
	private int figuareIndexOfAddMemory(List<Condition> conditions,BuildReteTempData buildReteTempData){
		for(Condition cond:conditions){
			if(buildReteTempData.isConditionJoin(cond.getExpression())){
				return conditions.size()-2;
			}
		}
		return conditions.size()-1;
	}
	/**
	 * 增加alphanode到当前的alphanodes
	 * @param alphaNode
	 */
	private void addAlphaNodeToAlphaNodes(AlphaNode alphaNode){
		if (!alphaNodes.containsKey(alphaNode.getConditionValue())) {
			alphaNodes.put(alphaNode.getConditionValue(),
					alphaNode);
		}else{
			AlphaNode exsitedNode=alphaNodes.get(alphaNode.getConditionValue());
			exsitedNode.mergeFromAnoterAlphaNode(alphaNode);
			
		}
	}
	
/*	public List<JoinNode> buildAlphaNode(Column column,
			BuildReteTempData reteTempData, String ruleName) {
		alphaNodes = alphaNodes == null ? new HashMap<String, AlphaNode>()
				: alphaNodes;
		if (column.getConditions() == null) {
			AlphaNode alphaNodeNull = new AlphaNode("", column.getTypeAllpath());
			alphaNodeNull.setRuleName(ruleName);
			if (!alphaNodes.containsKey("")) {
				alphaNodes.put("", alphaNodeNull);
				alphaNodeNull.buildNextNodes();
			}
			return null;
		}
		AlphaNode previousAlphaNode = null;
		int index = 0;
		List<JoinCondition> joinConditions = new ArrayList<JoinCondition>();
		AlphaMemoryNode leftInput = null;
		// 如果有joinCondition则所有的andOr都变成and
		makeConditionAndWhenJoinCondition(column, reteTempData);
		for (Condition condition : column.getConditions()) {
			index++;
			if (reteTempData.isConditionJoin(condition.getExpression())) {
				JoinCondition joinCondition = new JoinCondition();
				joinCondition.setCondition(condition);
				joinConditions.add(joinCondition);
				// column只有一个joinCondition就直接连接
				if (index == column.getConditions().size()) {
					if (index == 1) {
						if (!alphaNodes.containsKey("")) {
							AlphaNode alphaNode = new AlphaNode("",
									column.getTypeAllpath());
							alphaNode.setRuleName(ruleName);
							alphaNodes.put("", alphaNode);
						}
						leftInput = alphaNodes.get("").buildNextNodes();
						leftInput.setRuleName(ruleName);
					} else {
						leftInput = previousAlphaNode.buildNextNodes();
						leftInput.setRuleName(ruleName);
					}
				}
			} else {
				AlphaNode alphaNode = new AlphaNode(condition.getExpression(),
						column.getTypeAllpath());
				alphaNode.setRuleName(ruleName);
				alphaNode.setVariable(reteTempData.getVariableList(
						condition.getExpression(), column.getTypeAllpath())
						.get(0));
				if (condition.getAndOr() == AndOr.AND) {
					// 之前有父alphaNode
					if (previousAlphaNode != null) {
						previousAlphaNode.buildNextNodes(alphaNode);
					} else {
						if (!alphaNodes.containsKey(alphaNode
								.getConditionValue())) {
							alphaNodes.put(alphaNode.getConditionValue(),
									alphaNode);
						}
					}
				} else if (condition.getAndOr() == AndOr.OR) {
					// 如果为or，那之前的condition要连接到memorynode
					if (previousAlphaNode != null) {
						previousAlphaNode.buildNextNodes();
					}
					if (!alphaNodes.containsKey(alphaNode.getConditionValue())) {
						alphaNodes
								.put(alphaNode.getConditionValue(), alphaNode);
					}
				}
				// 最后一个condition要连接到memorynode
				if (index == column.getConditions().size()) {
					leftInput = alphaNodes.get(condition.getExpression())
							.buildNextNodes();
					leftInput.setRuleName(ruleName);
				}
				previousAlphaNode = alphaNode;
			}

		}
		// 若有joinCondition,则所有的这些condition都加上leftInput
		for (JoinCondition joinCondition : joinConditions) {
			joinCondition.setLeftInputNode(leftInput);
		}
		// 够在betanode
		return createBetaNode(joinConditions, reteTempData);
	}*/

	private void makeConditionAndWhenJoinCondition(Column column,
			BuildReteTempData reteTempData) {
		boolean isJoinCondition = false;
		for (Condition condition : column.getConditions()) {
			if (reteTempData.isConditionJoin(condition.getExpression())) {
				isJoinCondition = true;
				break;
			}
		}
		for (Condition condition : column.getConditions()) {
			condition.setAndOr("AND");
		}
	}

	/*private List<JoinNode> createBetaNode(List<JoinCondition> joinConditions,
			BuildReteTempData reteTempData) {
		List<JoinNode> results = new ArrayList<JoinNode>();
		if (joinConditions != null) {
			for (JoinCondition joinCondition : joinConditions) {
				JoinNode joinNode = new JoinNode(joinCondition.getCondition()
						.getExpression());
				joinNode.setRuleName(ruleName);
				joinCondition.getLeftInputNode().setJoinNode(joinNode);
				joinNode.setLeftInputNode(joinCondition.getLeftInputNode());
				String leftVar = reteTempData.getVariableList(
						joinCondition.getLeftInputNode().getPreviousNode()
								.getConditionValue(),
						joinCondition.getLeftInputNode().getPreviousNode().getObjectTypeString()).get(0);
				joinNode.setLeftVariable(leftVar);
				results.add(joinNode);
			}
		}
		return results;
	}*/

	public List<AlphaMemoryNode> traverseAndFindAlphaMemoryNodes(Rule rule) {
		List<AlphaMemoryNode> alphaMemoryNodes = new ArrayList<AlphaMemoryNode>();
		Iterator it = alphaNodes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Entry) it.next();
			AlphaNode alphaNode = (AlphaNode) entry.getValue();
			if (alphaNode.getRuleName().equals(rule.getName())) {
				alphaMemoryNodes.addAll(alphaNode
						.traverseAndFindAlphaMemoryNodes(rule));
			}
		}
		return alphaMemoryNodes;
	}

	// private HashSet<String> needToLinkAlphaMemNode(List<Condition>
	// conditions){
	// HashSet<String> hs=new HashSet<String>();
	// for(int i=0;i<conditions.size();i++){
	// if(i+1<conditions.size() && conditions.get(i+1).getAndOr()==AndOr.OR){
	// hs.add(conditions.get(i).getExpression());
	// }
	// }
	// }
	public List<AlphaMemoryNode> insert(Object obj) {
		List<AlphaMemoryNode> alphaMemoryNodes = new ArrayList<AlphaMemoryNode>();
		Iterator it = alphaNodes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Entry) it.next();
			AlphaNode alphaNode = (AlphaNode) entry.getValue();
			if (alphaNode.eval(obj)) {
				alphaMemoryNodes.addAll(alphaNode.insert(obj));
			}
		}
		return alphaMemoryNodes;

	}
	public void dispose(){
		for(Map.Entry entry:alphaNodes.entrySet()){
			((AlphaNode)entry.getValue()).dispose();
		}
	}
}

