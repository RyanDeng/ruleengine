package cn.dc.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dc.runtime.SalienceInteger;

public class Rule implements Serializable{
	private static final long        serialVersionUID = 510l;


    /**   */
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------
//    /** 包名 */
//    private String pkg;

	private RulePackage containerPackage;

    /** rule name */
    private String name;

    /** Salience value. */
    private int salience;
    

    
    private List<Column> columns;
    
    private String then;
    
//    /** Consequence. */
//    private Consequence consequence;
//
//    private Map<String, Consequence> namedConsequence;


    /** Load order in Package */
    private long loadOrder;

//    /** Is recursion of this rule allowed */
//    private boolean                  noLoop;

    
    private boolean	enabled;

//    public Rule() {
//    	
//    }

    /**
     * Construct a
     * <code>Rule<code> with the given name for the specified pkg parent
     *
     * @param name
     *            The name of this rule.
     */
    public Rule(final String name,
                final RulePackage pkg) {
        this.name = name;
        this.containerPackage=pkg;
        this.enabled = true;
        this.salience = SalienceInteger.DEFAULT_SALIENCE;
        this.containerPackage=pkg;
    }


    public Rule(final String name) {
        this( name,
              null);
    }
    /**
     * 把rule xml中的type与import的package对应起来
     */
    public void setAndFindColumnTypeAllPath(){
    	for(Column column:columns){
    		for(String allPath:containerPackage.getImportPkgs()){
    			if(typeInImportPkgs(column.getType(), allPath)){
    				column.setTypeAllpath(allPath);
    			}
    		}
    	}
    }
    private boolean typeInImportPkgs(String type,String pkgPath){
    	Pattern pattern = Pattern.compile(type+"$");
		Matcher matcher = pattern.matcher(pkgPath);
		boolean match = matcher.find();
		return match;
    }


	public RulePackage getContainerPackage() {
		return containerPackage;
	}


	public void setContainerPackage(RulePackage containerPackage) {
		this.containerPackage = containerPackage;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getSalience() {
		return salience;
	}


	public void setSalience(int salience) {
		this.salience = salience;
	}


	public List<Column> getColumns() {
		return columns;
	}


	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}


	public String getThen() {
		return then;
	}


	public void setThen(String then) {
		this.then = then.replaceAll("\\s", "");
	}


	public long getLoadOrder() {
		return loadOrder;
	}


	public void setLoadOrder(long loadOrder) {
		this.loadOrder = loadOrder;
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
    
}
