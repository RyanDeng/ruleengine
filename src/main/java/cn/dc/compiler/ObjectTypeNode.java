package cn.dc.compiler;

import java.io.Serializable;

public class ObjectTypeNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ObjectType objectType;

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}
	
}
