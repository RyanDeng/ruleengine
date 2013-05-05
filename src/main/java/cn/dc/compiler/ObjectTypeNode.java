package cn.dc.compiler;

import java.io.Serializable;
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

public class ObjectTypeNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ObjectType objectType;
	private String pkgName;

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
	public void  buildAlphaNode(Column column,BuildReteTempData reteTempData){
		alphaNodes= alphaNodes==null?new HashMap<String, AlphaNode>():alphaNodes;
		if(column.getConditions()==null){
			AlphaNode alphaNodeNull = new AlphaNode("");
			if(!alphaNodes.containsKey("")){
				alphaNodes.put("", alphaNodeNull);
				alphaNodeNull.buildNextNodes();
			}
			return;
		}
		AlphaNode previousAlphaNode=null;
		int index=0;
		HashMap<K, V>
		for(Condition condition:column.getConditions()){
			index++;
			if(reteTempData.isConditionJoin(condition.getExpression())){
				//TODO
			}else{
				AlphaNode alphaNode=new AlphaNode(condition.getExpression());
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
					alphaNodes.get(alphaNode.getConditionValue()).buildNextNodes();
				}
				previousAlphaNode=alphaNode;
			}
		}
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
