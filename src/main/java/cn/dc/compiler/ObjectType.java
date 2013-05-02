package cn.dc.compiler;

public class ObjectType {
	private String classNameAllPath;
	
	public ObjectType(String classNameAllString) {
		this.classNameAllPath=classNameAllString;
	}

	public String getClassNameAllPath() {
		return classNameAllPath;
	}

	public void setClassNameAllPath(String classNameAllPath) {
		this.classNameAllPath = classNameAllPath;
	}
	
}
