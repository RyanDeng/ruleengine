package cn.dc.ruleengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.attribute.HashAttributeSet;

import cn.dc.core.Condition.AndOr;
import cn.dc.use.UserAccount;
import cn.dc.util.StringParser;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Pattern pattern = Pattern.compile("Useraccount$");
//		Matcher matcher = pattern.matcher("cn.dc.UserAccount34");
//		boolean match = matcher.find();
//		if (match) {
//			System.out.println(true);
//		}else{
//			System.out.println(false);
//			AndOr andOr=AndOr.valueOf("and");
//			System.out.println(andOr);
//		}
//		Map<Integer,String> map=new HashMap<Integer, String>();
//		map.put(1, "a");
//		map.put(2,"b");
//		if(map.containsKey(1)){
//			
//		}else{
//		map.put(1, "c");
//		}
//		System.out.println(map);
		UserAccount account=new UserAccount();
		System.out.println(account.getClass().getName());
//		List<Integer> as=new ArrayList<Integer>();
//		as.add(1);as.add(2);
//		System.out.println(as.get(2));
	}

}
