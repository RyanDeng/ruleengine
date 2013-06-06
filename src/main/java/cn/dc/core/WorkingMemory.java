package cn.dc.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.swing.text.DefaultEditorKit.InsertBreakAction;

import cn.dc.agenda.Agenda;
import cn.dc.compiler.AlphaMemoryNode;
import cn.dc.compiler.JoinNode;
import cn.dc.compiler.Node;
import cn.dc.compiler.RuleNode;

public class WorkingMemory {

	private RuleBase ruleBase;
	private List<Object> memoryList;
	private Set<AlphaMemoryNode> alphaMemoryNodes;
	private List<AlphaMemoryNode> traversedAlphaMemoryNodes;
	private Queue<RuleNode> ruleQueue;
	private Agenda agenda;
	
	public WorkingMemory(RuleBase ruleBase) {
		this.ruleBase = ruleBase;
		memoryList = new ArrayList<Object>();
		alphaMemoryNodes = new HashSet<AlphaMemoryNode>();
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
		System.out.println("===开始执行===");
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
		System.out.println("====执行结束====");
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
			
			if(nodeExistedInTraversedNode(alphaMemoryNode)) continue;//如果存在则说明左或右节点已进入运算过
			
			if (alphaMemoryNode.getJoinNodes().size()!=0) {
				for(JoinNode joinNode:alphaMemoryNode.getJoinNodes()){
					if(joinNode instanceof RuleNode){
						ruleQueue.offer((RuleNode)joinNode);
					}else if (joinNode instanceof JoinNode) {
						if(joinNode.getRightInputNode() instanceof JoinNode) continue;
						RuleNode ruleNode = joinNode.fireEval(
								alphaMemoryNode);		
						if (ruleNode != null) {
							//////////////////把当前的另一边输入加入已traverse队列中
							putOtherNodesToTraverseNodes(alphaMemoryNode,joinNode);
							
							///////////
							ruleQueue.offer(ruleNode);
						}
					}
				}
			}
		}
	}
	private void putOtherNodesToTraverseNodes(Node oneSideNode, JoinNode joinNode){
		//////////////////把当前的另一边输入加入已traverse队列中
		AlphaMemoryNode otherSideNode = joinNode.getOtherInputNode(
				oneSideNode);
		if (otherSideNode != null) {
			traversedAlphaMemoryNodes.add(otherSideNode);
		}
		if(joinNode.getNextJoinOrRuleNode() instanceof JoinNode){
			putOtherNodesToTraverseNodes(joinNode, (JoinNode)joinNode.getNextJoinOrRuleNode());
		}
		///////////
	}
}
