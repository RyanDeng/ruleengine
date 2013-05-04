package cn.dc.ruleengine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dc.core.Condition.AndOr;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Pattern pattern = Pattern.compile("Useraccount$");
		Matcher matcher = pattern.matcher("cn.dc.UserAccount34");
		boolean match = matcher.find();
		if (match) {
			System.out.println(true);
		}else{
			System.out.println(false);
			AndOr andOr=AndOr.valueOf("and");
			System.out.println(andOr);
		}
		
	}

}
