package cn.dc.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.text.DefaultEditorKit.InsertBreakAction;

import cn.dc.agenda.Agenda;
import cn.dc.compiler.AlphaMemoryNode;
import cn.dc.compiler.JoinNode;
import cn.dc.compiler.RuleNode;

public class WorkingMemory {

	private RuleBase ruleBase;
	private List<Object> memoryList;
	private List<AlphaMemoryNode> alphaMemoryNodes;
	private List<AlphaMemoryNode> traversedAlphaMemoryNodes;
	private Queue<RuleNode> ruleQueue;
	private Agenda agenda;
	
	public WorkingMemory(RuleBase ruleBase) {
		this.ruleBase = ruleBase;
		memoryList = new ArrayList<Object>();
		alphaMemoryNodes = new ArrayList<AlphaMemoryNode>();
		traversedAlphaMemoryNodes = new ArrayList<AlphaMemoryNode>();
		ruleQueue=new LinkedList<RuleNode>();
		agenda=new Agenda();
	}

	public Agenda getAgenda() {
		return agenda;

	}

	public void insert(Object object) {
		memoryList.add(object);

	}

	public RuleBase getRuleBase() {
		return ruleBase;
	}

	public int fireAllRules() {
		insertObjects();
		getRuleQueue();
		
		for(RuleNode ruleNode:ruleQueue){
			agenda.insertIntoAgenda(ruleNode);
		}
		agenda.selectRuleToFire();
		return 0;
	}


	public void dispose() {
		ruleBase.getEntryPoint().dispose();

		agenda.dispose();
	}

	/**
	 * 判断joinNode的left和right是否都在alphaMemoryNodes里
	 * 
	 * @return
	 */
	private boolean nodeExistedInTraversedNode(AlphaMemoryNode alphaMemoryNode) {
		for(AlphaMemoryNode node:traversedAlphaMemoryNodes){
			if(node==alphaMemoryNode){
				return true;
			}
		}
		return false;
	}
	private void  insertObjects(){
		for (Object obj : memoryList) {
			List<AlphaMemoryNode> results = ruleBase.getEntryPoint()
					.insert(obj);
			if (results != null) {
				alphaMemoryNodes.addAll(results);
			}
		}
	}
	private void getRuleQueue(){
		for (AlphaMemoryNode alphaMemoryNode : alphaMemoryNodes) {
			if(nodeExistedInTraversedNode(alphaMemoryNode)) break;
			
			if (alphaMemoryNode.getJoinNode() != null) {
				if (alphaMemoryNode.getJoinNode() instanceof JoinNode) {
					RuleNode ruleNode = alphaMemoryNode.getJoinNode().fireEval(
							alphaMemoryNode);		
					if (ruleNode != null) {
						//////////////////把当前的另一边输入加入已traverse队列中
						AlphaMemoryNode otherSideNode = alphaMemoryNode
								.getJoinNode().getOtherInputNode(
										alphaMemoryNode);
						if (otherSideNode != null) {
							traversedAlphaMemoryNodes.add(otherSideNode);
						}
						///////////
						ruleQueue.offer(ruleNode);
					}
				}else if(alphaMemoryNode.getJoinNode() instanceof RuleNode){
					ruleQueue.offer((RuleNode)alphaMemoryNode.getJoinNode());
				}
			}
		}
	}
}
