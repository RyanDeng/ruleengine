package cn.dc.use;

import java.util.ArrayList;



public class Test3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyRuleEngine demoRuleEngine = new MyRuleEngineImpl();
		demoRuleEngine.initEngine();
		
		User userAccount=new User();
		userAccount.setUserId("1");
		Account account=new Account();
		account.setUserId("1");
		account.setBalance(20.0);
		Account account1=new Account();
		account1.setUserId("1");
		account1.setBalance(42.0);
		
		ArrayList<Object> objs=new ArrayList<Object>();
		objs.add(userAccount);
		objs.add(account);
		objs.add(account1);
		demoRuleEngine.executeRuleEngine(objs);
	}

}
