package cn.sse.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.dc.compiler.PackageBuilder;
import cn.dc.core.RuleBase;
import cn.dc.core.RuleBaseFactory;
import cn.dc.core.WorkingMemory;
import cn.sse.demo.bean.Company;
import cn.sse.demo.bean.Course;
import cn.sse.demo.bean.Department;
import cn.sse.demo.bean.Employee;
import cn.sse.demo.bean.Student;


public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
    	App app=new App();
    	PackageBuilder pBuilder=app.getPackageBuilderFromXmlFile();
		
		RuleBase ruleBase=RuleBaseFactory.newRuleBase();
		ruleBase.addPackages(pBuilder.getPackages());
		WorkingMemory workingMemory=ruleBase.newWorkingMemory();
		
		List<Object> objects=app.testInstances();
		
		List<String> copies=new ArrayList<String>();
		
		for(Object obj:objects){
			workingMemory.insert(obj);
			System.out.println("......"+obj.toString());
			copies.add(obj.toString());
		}
		
		workingMemory.fireAllRules();
		
		for(int i=0;i<objects.size();i++){
			if(objects.get(i).toString().equals(copies.get(i))){
//				System.out.println("......"+objects.get(i).toString());
//				Thread.sleep(200);
			}
			else { 
				System.err.println("......"+objects.get(i).toString());
				Thread.sleep(200);
			}
		}
		workingMemory.dispose();
    }
    public List<Object> testInstances(){
    	List<Object> objects=new ArrayList<Object>();
    	
    	Student s1=new Student(1, "李海", 5);
    	Student s2=new Student(2, "张三", 6);
    	Course c1_1=new Course(1, "math", 85, "待定");
    	Course c1_2=new Course(1, "chinese", 79, "待定");
    	Course c2_1=new Course(2, "math", 82, "待定");
    	Course c2_2=new Course(2, "chinese", 60, "待定");
    	
    	Company co1=new Company(1,"上海", "嘉定", 5);
    	Company co2=new Company(2,"上海", "杨浦", 15);
    	Company co3=new Company(3,"上海", "浦东", 7);
    	Company co4=new Company(4,"浙江", "杭州", 6);
    	
    	Department de1=new Department(1, 1, "市场部");
    	Department de2=new Department(2, 1, "行政部");
    	Department de3=new Department(3, 2, "市场部");
    	Department de4=new Department(4, 4, "市场部");
    	
    	Employee em1=new Employee(1, 1, 4, 300);
    	Employee em2=new Employee(2, 1, 6, 600);
    	Employee em3=new Employee(3, 2, 5, 800);
    	Employee em4=new Employee(4, 4, 6, 200);
		
		objects.add(s1);
		objects.add(s2);
		objects.add(c1_1);
		objects.add(c1_2);
		objects.add(c2_1);
		objects.add(c2_2);
		
		objects.add(co1);
		objects.add(co2);
		objects.add(co3);
		objects.add(co4);
		
		objects.add(de1);
		objects.add(de2);
		objects.add(de3);
		objects.add(de4);

		objects.add(em1);
		objects.add(em2);
		objects.add(em3);
		objects.add(em4);
		
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
