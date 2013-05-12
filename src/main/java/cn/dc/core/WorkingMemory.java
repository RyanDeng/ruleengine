package cn.dc.core;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.DefaultEditorKit.InsertBreakAction;

import cn.dc.compiler.AlphaMemoryNode;

public class WorkingMemory {

	private RuleBase ruleBase;
	private List<Object> memoryList;
	private List<AlphaMemoryNode> alphaMemoryNodes;
	
	public WorkingMemory(RuleBase ruleBase){
		this.ruleBase=ruleBase;
		memoryList=new ArrayList<Object>();
		alphaMemoryNodes=new ArrayList<AlphaMemoryNode>();
	}
	public Agenda getAgenda(){
		return null;
		
	}
	public void insert(Object object){
		memoryList.add(object);
		
	}
	public RuleBase getRuleBase(){
		return ruleBase;
	}

	public int fireAllRules(){
		for(Object obj:memoryList){
			List<AlphaMemoryNode> results=ruleBase.getEntryPoint().insert(obj);
			if(results!=null){
				alphaMemoryNodes.addAll(results);
			}
		}
		
		for(AlphaMemoryNode alphaMemoryNode:alphaMemoryNodes){
			if (alphaMemoryNode.getJoinNode()!=null) { 
				alphaMemoryNode.getJoinNode
			}
		}
	}

	public int fireAllRules(int fireLimit) throws Exception{
		return fireLimit;
		
	}
	public void dispose(){
		//TODO:
	}
	/**
	 * 判断joinNode的left和right是否都在alphaMemoryNodes里
	 * @return
	 */
	private boolean joinNodeWithBothLeftAndRight(){
		
	}
}
