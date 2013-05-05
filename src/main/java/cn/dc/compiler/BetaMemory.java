package cn.dc.compiler;

import java.util.HashMap;
import java.util.List;

public class BetaMemory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<HashMap<String,Object>> readyObjects;
	public List<HashMap<String, Object>> getReadyObjects() {
		return readyObjects;
	}
	public void setReadyObjects(List<HashMap<String, Object>> readyObjects) {
		this.readyObjects = readyObjects;
	}
	
}
