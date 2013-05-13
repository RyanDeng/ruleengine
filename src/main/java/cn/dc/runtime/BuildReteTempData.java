package cn.dc.runtime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mvel2.optimizers.impl.refl.nodes.ArrayAccessor;

import cn.dc.compiler.AlphaMemoryNode;
import cn.dc.compiler.ObjectType;

public class BuildReteTempData {
	private Map<String,String> variables;
	//private List<String> objectTypeNames;
	public BuildReteTempData(Map<String,String> variables){
		this.variables=variables;
		//this.objectTypeNames=objectTypeNames;
	}
	public Map<String,String> getVariables() {
		return variables;
	}
	public void setVariables(Map<String,String> variables) {
		this.variables = variables;
	}
	public boolean isConditionJoin(String expression){
		int count=0;
		Iterator it=variables.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry entry=(Entry)it.next();
			if(expression.indexOf(entry.getValue().toString())!=-1){
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
	public List<String> getVariableList(String expre,String classPath){
		List<String> results=new ArrayList<String>();
		Iterator it=variables.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry entry=(Entry)it.next();
			if(expre.indexOf(entry.getValue().toString())!=-1){
				results.add(entry.getValue().toString());
			}
		}
		if(results.size()==0){
			results.add(variables.get(classPath));
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
