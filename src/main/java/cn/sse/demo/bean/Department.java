package cn.sse.demo.bean;

public class Department {
	private int id;
	private int companyId;
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Department(int id, int companyId, String name) {
		super();
		this.id = id;
		this.companyId = companyId;
		this.name = name;
	}
	@Override
	public String toString() {
		return "Department [id=" + id + ", companyId=" + companyId + ", name="
				+ name + "]";
	}
	
}
