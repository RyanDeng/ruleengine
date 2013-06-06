package cn.sse.demo.bean;

public class Student {

	private int id;
	private String name;
	private int award;
	public Student(int id, String name,int award) {
		super();
		this.id = id;
		this.name = name;
		this.award = award;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAward() {
		return award;
	}
	public void setAward(int award) {
		this.award = award;
	}
	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", award=" + award
				+ "]";
	}
	

}
