package cn.sse.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.dc.compiler.PackageBuilder;
import cn.dc.core.RuleBase;
import cn.dc.core.RuleBaseFactory;
import cn.dc.core.WorkingMemory;
import cn.sse.demo.bean.Account;
import cn.sse.demo.bean.Course;
import cn.sse.demo.bean.Student;
import cn.sse.demo.bean.User;


public class App 
{
    public static void main( String[] args )
    {
    	App app=new App();
    	PackageBuilder pBuilder=app.getPackageBuilderFromXmlFile();
		
		RuleBase ruleBase=RuleBaseFactory.newRuleBase();
		ruleBase.addPackages(pBuilder.getPackages());
		WorkingMemory workingMemory=ruleBase.newWorkingMemory();
		
		List<Object> objects=app.testInstances();
		
		for(Object obj:objects){
			workingMemory.insert(obj);
		}
		workingMemory.fireAllRules();
		
		workingMemory.dispose();
    }
    public List<Object> testInstances(){
    	List<Object> objects=new ArrayList<Object>();
//		User user=new User();
//		user.setUserId("2");
//		user.setBalance(50.0);
//		user.setAge(22);
//		user.setGender("male");
//		user.setAddress("上海");
//		user.setCompany("电信");
//		user.setPhone("13524");
//		user.setHeight(176);
//		
//		Account account1=new Account();
//		account1.setUserId("2");
//		account1.setBalance(20.0);
//		
//		Account account2=new Account();
//		account2.setUserId("2");
//		account2.setBalance(42.0);
    	
    	Student s1=new Student(1, "李海", 5);
    	Student s2=new Student(2, "张三", 6);
    	Course c1_1=new Course(1, "math", 85, "待定");
    	Course c1_2=new Course(1, "chinese", 79, "待定");
    	Course c2_1=new Course(2, "math", 82, "待定");
    	Course c2_2=new Course(2, "chinese", 60, "待定");
		
		objects.add(s1);
		objects.add(s2);
		objects.add(c1_1);
		objects.add(c1_2);
		objects.add(c2_1);
		objects.add(c2_2);
		
		return objects;
    }
    
    public PackageBuilder getPackageBuilderFromXmlFile() {

		List<String> xmlFilePath = getDiretoryXmlFile("/home/ryan/workspace/ruleengine/src/main/java/cn/sse/rules/");
		PackageBuilder packageBuilder = new PackageBuilder();
		// new ClassPathResource("com/vipshop/drl/testDyn.drl"));
		for (String r : xmlFilePath) {
			packageBuilder.addPackageFromXmlPath(r);
		}

		return packageBuilder;
	}
    private List<String> getDiretoryXmlFile(String directory) {
		List<String> xmlFilePath = new ArrayList<String>();
		//File file = new File(MyRuleEngineImpl.class.getResource(directory).getFile());
		// get the folder list
		File file=new File(directory);
		File[] array = file.listFiles();

		for (int i = 0; i < array.length; i++) {
			if (array[i].isFile()) {
				xmlFilePath.add(array[i].getPath());
			} else if (array[i].isDirectory()) {
				getDiretoryXmlFile(array[i].getPath());
			}
		}
		return xmlFilePath;
	}
}
