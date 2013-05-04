package cn.dc.core;

import java.util.List;

public class ReteooRuleBase implements RuleBase {
	
	private ReteooBuilder reteooBuilder;
	
	public ReteooRuleBase(){
		reteooBuilder=new ReteooBuilder(this);
	}
	public List<RulePackage> getPackages() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void addPackage(RulePackage pkg) {
		reteooBuilder.buildReteoo(pkg);

	}

	public void lock() {
		// TODO Auto-generated method stub

	}

	public void unlock() {
		// TODO Auto-generated method stub

	}

	public int getAdditionsSinceLock() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getRemovalsSinceLock() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void removePackage(String packageName) {
		// TODO Auto-generated method stub

	}

	public void removeRule(String packageName, String ruleName) {
		// TODO Auto-generated method stub

	}

	public void removeQuery(String packageName, String queryName) {
		// TODO Auto-generated method stub

	}

	public void removeFunction(String packageName, String functionName) {
		// TODO Auto-generated method stub

	}

	public void removeProcess(String id) {
		// TODO Auto-generated method stub

	}

	public WorkingMemory newWorkingMemory() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addPackages(List<RulePackage> pkgs) {
		if(pkgs!=null){
			for(RulePackage rulePackage:pkgs){
				addPackage(rulePackage);
			}
		}
		
	}

	public WorkingMemory[] getWorkingMemories() {
		// TODO Auto-generated method stub
		return null;
	}


	public RulePackage getPackage(String name) {
		// TODO Auto-generated method stub
		return null;
	}




}
