package cn.dc.core;

public interface WorkingMemory {

	public Agenda getAgenda();

	RuleBase getRuleBase();

	int fireAllRules() throws Exception;

	int fireAllRules(int fireLimit) throws Exception;

	
}
