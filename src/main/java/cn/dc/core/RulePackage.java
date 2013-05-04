package cn.dc.core;

import java.util.List;

public class RulePackage {
	private String name;
    private List<String> importPkgs;
	private List<Rule> rules;
	
	public RulePackage(String name){
		this.name=name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Rule> getRules() {
		return rules;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
	public List<String> getImportPkgs() {
		return importPkgs;
	}
	public void setImportPkgs(List<String> importPkgs) {
		this.importPkgs = importPkgs;
	}
	public void  fillPackageForRules(){
		for(Rule rule:rules){
			rule.setContainerPackage(this);
			rule.setAndFindColumnTypeAllPath();
		}
	}
}
