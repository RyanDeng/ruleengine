package cn.dc.core;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.DefaultEditorKit.InsertBreakAction;

public class WorkingMemory {

	private RuleBase ruleBase;
	private List<Object> memoryList;
	
	public WorkingMemory(RuleBase ruleBase){
		this.ruleBase=ruleBase;
		memoryList=new ArrayList<Object>();
	}
	public Agenda getAgenda(){
		return null;
		
	}
	public void insert(Object object){
		memoryList.add(object);
		ruleBase.getEntryPoint().insert(object);
	}
	public RuleBase getRuleBase(){
		return ruleBase;
	}

	public int fireAllRules() throws Exception{
		return 0;
		
	}

	public int fireAllRules(int fireLimit) throws Exception{
		return fireLimit;
		
	}
	
	
}
