package cn.dc.agenda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.dc.compiler.AlphaMemoryNode;
import cn.dc.compiler.JoinNode;
import cn.dc.compiler.RuleNode;



public class Activation {
	private String ruleName;
	private String thenStatements;
	private int salience;
	List<Map<String, Object>> readyObjects;
	
	public Activation(){}
	public Activation(RuleNode node){
		this.ruleName=node.getRuleName();
		this.thenStatements=node.getThenStatements();
		this.salience=node.getSalience();
		readyObjects=new ArrayList<Map<String,Object>>();
		if(node.getPreviousNode() instanceof JoinNode){
			JoinNode previousNode=(JoinNode) node.getPreviousNode();
			if(previousNode!=null){
				readyObjects.addAll(previousNode.getBetaMemory().getReadyObjects());
			}
		}else if(node.getPreviousNode() instanceof AlphaMemoryNode){
			AlphaMemoryNode previousNode=(AlphaMemoryNode) node.getPreviousNode();
			if(previousNode!=null){
				List<Map<String, Object>> maps=new ArrayList<Map<String,Object>>();
				for(Object obj:previousNode.getReadyObjects()){
					Map<String, Object> map=new HashMap<String, Object>();
					map.put(node.getVarString(), obj);
					maps.add(map);
				}
				readyObjects.addAll(maps);
			}
		}
	}
	public String getRuleName() {
		return ruleName;
	}
	public String getThenStatements() {
		return thenStatements;
	}
	public int getSalience() {
		return salience;
	}
	public boolean isModifyObject(){
		if(readyObjects.size()>0){
			Iterator iterator=readyObjects.get(0).keySet().iterator();
			while(iterator.hasNext()){
				String varName=(String) iterator.next();
				if(thenStatements.indexOf(varName+".set")!=-1){
					return true;
				}
			}
		}
		return false;
	}
	public List<Map<String, Object>> getReadyObjects() {
		return readyObjects;
	}
	public void dispose(){
		readyObjects.clear();
	}
}
