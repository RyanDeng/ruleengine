package cn.dc.core;

import java.util.List;

/**
 * rule xml中的一条lhs单位是一个column
 * @author ryan
 *
 */
public class Column {
	private String type;
	private String typeAllpath;
	private String name;
	private List<Condition> conditions;
	
	public Column(String type,String name){
		this.type=type;
		this.name=name;
		this.typeAllpath=type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeAllpath() {
		return typeAllpath;
	}

	public void setTypeAllpath(String typeAllpath) {
		this.typeAllpath = typeAllpath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
	
	
}
