package cn.dc.util;

public class StringParser {
	public static String getclassName(Object obj){
		String classPath=obj.getClass().getName();
		int indexSpace=classPath.indexOf(' ');
		String className=classPath.substring(indexSpace+1);
		return className;
	}
}
