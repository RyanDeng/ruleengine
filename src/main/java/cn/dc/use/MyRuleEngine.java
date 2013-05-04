package cn.dc.use;

import java.util.List;

/**
 * ruleengine接口
 * @author 邓超
 *
 */
public interface MyRuleEngine {
	 /** 
     * 初始化
     */  
    public void initEngine();  
      
    /** 
     * 刷新规则引擎中的规则
     */  
    public void refreshEnginRule();  
      
    /** 
     * 执行规则引擎
     * @param objs .
     */  
    public void executeRuleEngine(final List<Object> objs);  
}
