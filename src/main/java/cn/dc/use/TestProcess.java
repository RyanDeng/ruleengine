package cn.dc.use;

import cn.dc.compiler.PackageBuilder;
import cn.dc.core.ReteooRuleBase;
import cn.dc.core.RuleBase;

public class TestProcess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PackageBuilder pBuilder=new PackageBuilder();
		pBuilder.addPackageFromXmlPath("/home/ryan/workspace/ruleengine/src/main/java/cn/dc/rules/account.xml");
		
		RuleBase ruleBase=new ReteooRuleBase();
		ruleBase.addPackages(pBuilder.getPackages());
	}
}
