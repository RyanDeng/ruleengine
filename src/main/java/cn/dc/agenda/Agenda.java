package cn.dc.agenda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.mvel2.MVEL;

import cn.dc.compiler.RuleNode;
import cn.dc.core.WorkingMemory;

public class Agenda {
	WorkingMemory workingMemory;
	HashSet<Activation> activationPool;
	List<Activation> readyQueue;
	public Agenda(){
		activationPool=new HashSet<Activation>();
		readyQueue=new ArrayList<Activation>();
	}
	
	WorkingMemory getWorkingMemory(){
		return workingMemory;
	}
	public void insertIntoAgenda(RuleNode node){
		Activation activation=new Activation(node);
		activationPool.add(activation);
	}
	public void selectRuleToFire(){
		for(Activation activation:activationPool){
			readyQueue.add(activation);
		}
		Collections.sort(readyQueue,new ConflictResolver());
		for(Activation activation:readyQueue){
			fire(activation);
		}
		
	}
	private void fire(Activation activation){
		System.out.println("====执行"+activation.getRuleName()+"====");
		Serializable compiledSerializable=MVEL.compileExpression(activation.getThenStatements());
		for(Map<String, Object> map:activation.getReadyObjects()){
			MVEL.executeExpression(compiledSerializable,map);
		}
	}
	public void dispose(){
		for(Activation activation:activationPool){
			activation.dispose();
		}
		for(Activation activation:readyQueue){
			activation.dispose();
		}
		activationPool.clear();
		readyQueue.clear();
	}
}
