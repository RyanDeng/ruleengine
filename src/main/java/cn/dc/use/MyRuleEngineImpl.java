package cn.dc.use;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import cn.dc.compiler.PackageBuilder;
import cn.dc.core.RuleBase;


/**
 * ruleengine实现类
 * 
 * @author 邓超
 * 
 */
public class MyRuleEngineImpl implements MyRuleEngine {
	private RuleBase ruleBase;

	public void initEngine() {
		// TODO Auto-generated method stub
		// 设置时间格式
		System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
		ruleBase = MyRuleBaseFactory.getRuleBase();
		try {
			PackageBuilder packageBuilder = getPackageBuilderFromDrlFile();
			ruleBase.addPackages(packageBuilder.getPackages());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void refreshEnginRule() {
//		ruleBase = MyRuleBaseFactory.getRuleBase();
//		org.drools.rule.Package[] packages = ruleBase.getPackages();
//		for (org.drools.rule.Package pg : packages) {
//			ruleBase.removePackage(pg.getName());
//		}
//
//		initEngine();
//	}

	public void executeRuleEngine(List<Object> objs) {
		if (null == ruleBase.getPackages()
				|| 0 == ruleBase.getPackages().length) {
			return;
		}
		StatefulSession statefulSession = ruleBase.newStatefulSession();
		if (objs != null) {
			for (Object obj : objs) {
				statefulSession.insert(obj);
			}
		}

		// fire
		statefulSession.fireAllRules(new org.drools.spi.AgendaFilter() {
			public boolean accept(Activation activation) {
				return !activation.getRule().getName().contains("_test");
			}
		});

		statefulSession.dispose();
	}

	private PackageBuilder getPackageBuilderFromDrlFile() throws Exception {

		List<String> drlFilePath = getTestDrlFile("/com/vipshop/drl/");
		List<Reader> readers = readRuleFromDrlFile(drlFilePath);
		PackageBuilder packageBuilder = new PackageBuilder();
		// new ClassPathResource("com/vipshop/drl/testDyn.drl"));
		for (Reader r : readers) {
			packageBuilder.addPackageFromDrl(r);
		}
		// 检查脚本是否有问题
		if (packageBuilder.hasErrors()) {
			throw new Exception(packageBuilder.getErrors().toString());
		}

		return packageBuilder;
	}

	private List<Reader> readRuleFromDrlFile(List<String> drlFilePath)
			throws FileNotFoundException {
		if (null == drlFilePath || 0 == drlFilePath.size()) {
			return null;
		}

		List<Reader> readers = new ArrayList<Reader>();

		for (String ruleFilePath : drlFilePath) {
			readers.add(new FileReader(new File(ruleFilePath)));
		}

		return readers;
	}

	private List<String> getTestDrlFile(String directory) {
		List<String> drlFilePath = new ArrayList<String>();
		File file = new File(MyRuleEngineImpl.class.getResource(directory).getFile());
		// get the folder list
		File[] array = file.listFiles();

		for (int i = 0; i < array.length; i++) {
			if (array[i].isFile()) {
				drlFilePath.add(array[i].getPath());
			} else if (array[i].isDirectory()) {
				getTestDrlFile(array[i].getPath());
			}
		}
		return drlFilePath;
		// List<String> drlFilePath = new ArrayList<String>();
		// drlFilePath
		// .add("C:/test.drl");
		// return drlFilePath;
	}

}
