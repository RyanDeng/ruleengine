package cn.dc.core;

public class RuleBaseFactory {
	private static RuleBase ruleBase=null;
	public static RuleBase newRuleBase(){
		if(ruleBase==null){
			ruleBase=new ReteooRuleBase();
		}
		return ruleBase;
	}
}
