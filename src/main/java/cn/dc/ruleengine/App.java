package cn.dc.ruleengine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExpressionCompiler;

import cn.dc.use.Account;
import cn.dc.use.UserAccount;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	//CompiledExpression exp=(CompiledExpression) MVEL.compileExpression("name");
    	String arString="abc";
    	String expwhe=new String("name == 'abc'");
    	Person person=new Person();
    	person.setName("nifd");
    	
    	
//    	ExpressionCompiler compiler = new ExpressionCompiler(expwhe);
//
//        ParserContext context = new ParserContext();
//        context.setStrictTypeEnforcement(true);
//
//        context.addInput("person", Person.class);
//
//        Serializable comExp=MVEL.compileExpression(expwhe,context);
//    	Object res=MVEL.executeExpression(comExp);
    	
        Object res=MVEL.eval(expwhe,person);
        System.out.println(res);
        System.out.println(person.getName());
        
        UserAccount userAccount2=new UserAccount();
        userAccount2.setUserId("2");
        Account account=new Account();
        account.setBalance(20);
        account.setUserId("3");
        
        
        Serializable compiled =  MVEL.compileExpression("userAccount.getUserId() < account.userId");
        Map vars = new HashMap();
        vars.put("userAccount", userAccount2);
        vars.put("account", account);
        Object res1=MVEL.executeExpression(compiled,vars);
        System.out.println(res1);
        
        System.out.println("==~~~~~~~~~~~~~~~~~~");
        Serializable compiledTODO =MVEL.compileExpression("account.setUserId('6');System.out.println(account.balance);");
        Map vars2=new HashMap();
        vars2.put("account", account);
        Object res2=MVEL.executeExpression(compiledTODO,vars2);
        System.out.println(res2);
        System.out.println("+"+account.getUserId());
        
        
        System.out.println("======================");
        Serializable compiled123 =MVEL.compileExpression("account.userId==userAccount.userId");
		Map vars123 = new HashMap();
		Account leftobj=new Account();
		leftobj.setUserId("1");
		leftobj.setBalance(42.0);
		UserAccount rightobj=new UserAccount();
		rightobj.setUserId("1");
		 vars.put("account", leftobj);
		 vars.put("userAccount", rightobj);
		 Boolean res123=(Boolean) MVEL.executeExpression(compiled123,vars);
		 System.out.println(res123);
    }
}
