package cn.dc.compiler;

import java.io.Serializable;
import java.util.HashMap;

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

	public AlphaMemoryNode insert(Object obj){
		ObjectTypeNode objectTypeNode=objectTypeNodes.get(obj.getClass().getName());
		if(objectTypeNode!=null){
			objectTypeNode.insert(obj);
		}
	}
	
}
