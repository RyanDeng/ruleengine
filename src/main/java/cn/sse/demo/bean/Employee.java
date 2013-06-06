package cn.sse.demo.bean;

public class Employee {
	private int id;
	private int departmentId;
	private int workAge;
	private int salary;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public int getWorkAge() {
		return workAge;
	}
	public void setWorkAge(int workAge) {
		this.workAge = workAge;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	@Override
	public String toString() {
		return "Employee [id=" + id + ", departmentId=" + departmentId + ", workAge="
				+ workAge + ", salary=" + salary + "]";
	}
	public Employee(int id, int departmentId, int workAge, int salary) {
		super();
		this.id = id;
		this.departmentId = departmentId;
		this.workAge = workAge;
		this.salary = salary;
	}
	
	
}
