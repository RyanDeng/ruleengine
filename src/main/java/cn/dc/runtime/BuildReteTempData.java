package cn.dc.runtime;

import java.util.List;

public class BuildReteTempData {
	private List<String> variables;
	//private List<String> objectTypeNames;
	public BuildReteTempData(List<String> variables){
		this.variables=variables;
		//this.objectTypeNames=objectTypeNames;
	}
	public List<String> getVariables() {
		return variables;
	}
	public void setVariables(List<String> variables) {
		this.variables = variables;
	}
	public boolean isConditionJoin(String expression){
		int count=0;
		for(String var:variables){
			if (expression.indexOf(var)!=-1) {
				count++;
			}
		}
		if(count>=2){
			return true;
		}
		else {
			return false;
		}
	}
//	public List<String> getObjectTypeNames() {
//		return objectTypeNames;
//	}
//	public void setObjectTypeNames(List<String> objectTypeNames) {
//		this.objectTypeNames = objectTypeNames;
//	}
	
}
