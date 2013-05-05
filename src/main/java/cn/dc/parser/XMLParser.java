package cn.dc.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.DefaultStyledDocument.ElementSpec;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.dc.core.Column;
import cn.dc.core.Condition;
import cn.dc.core.Rule;
import cn.dc.core.RulePackage;

public class XMLParser {
	
	
	private static Document getDocument(String path) throws DocumentException{
		SAXReader reader=new SAXReader();
		Document document=reader.read(new File(path));
		return document;
	}
	private static List<String> parseForImports(Element rootElm){
		List<String> importPkgs=new ArrayList<String>();
		List<Element> imports=rootElm.elements("import");
		for(Element importElement :imports){
			importPkgs.add(importElement.attributeValue("name").replaceAll("\\s", ""));
		}
		return importPkgs;
	}
	private static Rule parseForRule(Element ruleElm){
		Rule rule=new Rule(ruleElm.attributeValue("name"));
		Element rule_attrtibute=ruleElm.element("rule-attribute");
		if(rule_attrtibute!=null && rule_attrtibute.attributeValue("name").equals("salience")){
			rule.setSalience(Integer.parseInt(rule_attrtibute.attributeValue("value")));
		}
		
		rule.setColumns(new ArrayList<Column>());
		Element whenElement=ruleElm.element("when");
		List<Element> columnElements=whenElement.elements("column");
		for(Element element:columnElements){
			rule.getColumns().add(parseForColumn(element));
		}
		
		Element thenElement=ruleElm.element("then");
		rule.setThen(thenElement.getText());
		return rule;
	}
	private static Column parseForColumn(Element columnElm){
		Column column=new Column(columnElm.attributeValue("type").replaceAll("\\s", ""), columnElm.attributeValue("name").replaceAll("\\s", ""));
		column.setConditions(new ArrayList<Condition>());
		List<Element> conditions=columnElm.elements("condition");
		for(Element element:conditions){
			column.getConditions().add(parseForCondition(element));
		}
		return column;
	}
	private static Condition parseForCondition(Element conditionElm){
		Condition condition=new Condition(conditionElm.getText().replaceAll("\\s", ""));
		if(conditionElm.attribute("andOr")!=null){
			condition.setAndOr(conditionElm.attributeValue("andOr").replaceAll("\\s", ""));
		}
		return condition;
	}
	public static RulePackage parseForPackage(String path){
		try {
			Document document=XMLParser.getDocument(path);
			Element rootElm = document.getRootElement();
			
			RulePackage rulePackage=new RulePackage(rootElm.attributeValue("name").replaceAll("\\s", ""));
			//解析得到import
			rulePackage.setImportPkgs(parseForImports(rootElm));
			
			rulePackage.setRules(new ArrayList<Rule>());
			List<Element> ruleElements=rootElm.elements("rule");
			for(Element ruleElement:ruleElements){
				rulePackage.getRules().add(parseForRule(ruleElement));
			}
			rulePackage.fillPackageForRules();
			return rulePackage;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("文件路径不正确");
		}
		return null;
		
		
	}
//	public static Rule parseForRules(String path) {
//		Document document=getDocument(path);
//	}
	
	public static void main(String[] args){
		XMLParser.parseForPackage("/home/ryan/workspace/ruleengine/src/main/java/cn/dc/rules/account.xml");
	}
}
