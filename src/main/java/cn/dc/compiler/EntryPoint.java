package cn.dc.compiler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryPoint implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, ObjectTypeNode> objectTypeNodes;
	public HashMap<String, ObjectTypeNode> getObjectTypeNodes() {
		return objectTypeNodes;
	}
	public void setObjectTypeNodes(HashMap<String, ObjectTypeNode> objectTypeNodes) {
		this.objectTypeNodes = objectTypeNodes;
	}

	public List<AlphaMemoryNode> insert(Object obj){
		ObjectTypeNode objectTypeNode=objectTypeNodes.get(obj.getClass().getName());
		if(objectTypeNode!=null){
			return objectTypeNode.insert(obj);
		}
		return null;
	}
	public void dispose(){
		for(Map.Entry entry:objectTypeNodes.entrySet()){
			((ObjectTypeNode)entry.getValue()).dispose();
		}
	}
	
}
