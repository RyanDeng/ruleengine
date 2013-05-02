package cn.dc.compiler;

import java.io.Serializable;
import java.util.HashMap;

public class ObjectTypeNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ObjectType objectType;

	private HashMap<String, AlphaNode> alphaNodes;

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}
	
}
