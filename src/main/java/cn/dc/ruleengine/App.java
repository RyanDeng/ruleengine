package cn.dc.ruleengine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExpressionCompiler;


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
        Serializable compiledTODO =MVEL.compileExpression("System.out.println(account.balance);System.out.println(account.getUserId())");
        Map vars2=new HashMap();
        vars2.put("account", account);
        Object res2=MVEL.executeExpression(compiledTODO,vars2);
        System.out.println(res2);
    }
}
