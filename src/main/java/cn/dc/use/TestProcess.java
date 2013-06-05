package cn.dc.use;

import cn.dc.compiler.PackageBuilder;
import cn.dc.core.ReteooRuleBase;
import cn.dc.core.RuleBase;
import cn.dc.core.WorkingMemory;

public class TestProcess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PackageBuilder pBuilder=new PackageBuilder();
		pBuilder.addPackageFromXmlPath("/home/ryan/workspace/ruleengine/src/main/java/cn/dc/rules/examplelogic.xml");
		
		RuleBase ruleBase=new ReteooRuleBase();
		ruleBase.addPackages(pBuilder.getPackages());
		WorkingMemory workingMemory=ruleBase.newWorkingMemory();
		
		User user=new User();
		user.setUserId("2");
		user.setBalance(50.0);
		user.setAge(22);
		user.setGender("male");
		user.setAddress("上海");
		user.setCompany("电信");
		user.setPhone("13524");
		user.setHeight(176);
		Account account=new Account();
		account.setUserId("2");
		account.setBalance(20.0);
		Account account1=new Account();
		account1.setUserId("2");
		account1.setBalance(42.0);
		
		workingMemory.insert(user);
		workingMemory.insert(account);
		workingMemory.insert(account1);
		
		workingMemory.fireAllRules();
		workingMemory.dispose();
	}
}
