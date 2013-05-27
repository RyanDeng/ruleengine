package cn.dc.core;

import java.util.HashMap;

public class Condition {
	private AndOr andOr;
	private String expression;
	private Column leftInput;
	private String bracket;
	public Condition(){this.andOr=AndOr.valueOf("AND");}
	public Condition(String expre){
		this.expression=expre;
		this.andOr=AndOr.valueOf("AND");
	}
	public Condition(String expr, String andOr)throws IllegalArgumentException{
		this.expression=expr;
		this.andOr=AndOr.valueOf(andOr.toUpperCase());
	}
	public AndOr getAndOr() {
		return andOr;
	}


	public void setAndOr(AndOr andOr) {
		this.andOr = andOr;
	}
	public void setAndOr(String andOr){
		this.andOr=AndOr.valueOf(andOr.toUpperCase());
	}

	public String getExpression() {
		return expression;
	}


	public void setExpression(String expression) {
		this.expression = expression;
	}


	public String getBracket() {
		return bracket;
	}
	public void setBracket(String bracket) {
		this.bracket = bracket;
	}


	public enum AndOr{
		AND,
		OR;
	}
	
}
