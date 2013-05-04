package cn.dc.use;



/**
 * rulebase工厂类
 * @author 邓超
 *
 */
public class MyRuleBaseFactory {
	private static RuleBase ruleBase;  
    
    public static RuleBase getRuleBase(){  
        return null != ruleBase ? ruleBase : RuleBaseFactory.newRuleBase();  
    }  
}
