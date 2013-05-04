package cn.dc.compiler;

import java.util.ArrayList;
import java.util.List;

import cn.dc.core.RulePackage;
import cn.dc.parser.XMLParser;

public class PackageBuilder {
	private List<RulePackage> rulePackages;
	
	public PackageBuilder(){
		rulePackages=new ArrayList<RulePackage>();
	}
	public void addPackageFromXmlPath(String path){
		RulePackage rulePackage=XMLParser.parseForPackage(path);
		rulePackages.add(rulePackage);
	}
	public List<RulePackage> getPackages() {
		return rulePackages;
	}
	
	
}
