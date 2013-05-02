package cn.dc.compiler;

import java.io.Serializable;
import java.util.HashMap;

public class RootNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, ObjectTypeNode> objectTypeMaps;

	public HashMap<String, ObjectTypeNode> getObjectTypeMaps() {
		return objectTypeMaps;
	}

	public void setObjectTypeMaps(HashMap<String, ObjectTypeNode> objectTypeMaps) {
		this.objectTypeMaps = objectTypeMaps;
	}
	
}
