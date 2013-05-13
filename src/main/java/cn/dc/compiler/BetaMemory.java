package cn.dc.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BetaMemory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<HashMap<String,Object>> readyObjects=new ArrayList<HashMap<String,Object>>();
	public List<HashMap<String, Object>> getReadyObjects() {
		return readyObjects;
	}
	public void setReadyObjects(List<HashMap<String, Object>> readyObjects) {
		this.readyObjects = readyObjects;
	}
	public void insert(HashMap<String, Object> map){
		readyObjects.add(map);
	}
}
