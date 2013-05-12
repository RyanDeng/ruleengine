package cn.dc.runtime;

import java.util.ArrayList;
import java.util.List;

import org.mvel2.optimizers.impl.refl.nodes.ArrayAccessor;

import cn.dc.compiler.ObjectType;

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
	public List<String> getVariableList(String expre){
		List<String> results=new ArrayList<String>();
		for(String var:variables){
			if(expre.indexOf(var)!=-1){
				results.add(var);
			}
		}
		return results;
	}
//	public List<String> getObjectTypeNames() {
//		return objectTypeNames;
//	}
//	public void setObjectTypeNames(List<String> objectTypeNames) {
//		this.objectTypeNames = objectTypeNames;
//	}
	
}
