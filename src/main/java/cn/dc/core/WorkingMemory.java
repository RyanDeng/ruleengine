package cn.dc.core;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.DefaultEditorKit.InsertBreakAction;

import cn.dc.compiler.AlphaMemoryNode;
import cn.dc.compiler.JoinNode;
import cn.dc.compiler.RuleNode;

public class WorkingMemory {

	private RuleBase ruleBase;
	private List<Object> memoryList;
	private List<AlphaMemoryNode> alphaMemoryNodes;
	private List<AlphaMemoryNode> traversedAlphaMemoryNodes;

	public WorkingMemory(RuleBase ruleBase) {
		this.ruleBase = ruleBase;
		memoryList = new ArrayList<Object>();
		alphaMemoryNodes = new ArrayList<AlphaMemoryNode>();
		traversedAlphaMemoryNodes = new ArrayList<AlphaMemoryNode>();
	}

	public Agenda getAgenda() {
		return null;

	}

	public void insert(Object object) {
		memoryList.add(object);

	}

	public RuleBase getRuleBase() {
		return ruleBase;
	}

	public int fireAllRules() {
		for (Object obj : memoryList) {
			List<AlphaMemoryNode> results = ruleBase.getEntryPoint()
					.insert(obj);
			if (results != null) {
				alphaMemoryNodes.addAll(results);
			}
		}

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
						
					}
				}
			}
		}
	}

	public int fireAllRules(int fireLimit) throws Exception {
		return fireLimit;

	}

	public void dispose() {
		// TODO:
	}

	/**
	 * 判断joinNode的left和right是否都在alphaMemoryNodes里
	 * 
	 * @return
	 */
	private boolean nodeExistedInTraversedNode(AlphaMemoryNode alphaMemoryNode) {
		for(AlphaMemoryNode node:traversedAlphaMemoryNodes){
			if(node==)
		}

	}
}
