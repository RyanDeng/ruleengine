package cn.sse.demo.bean;

public class Course {
	private int studentId;
	private String name;
	private int score;
	private String level;
	
	
	public Course(int studentId, String name, int score, String level) {
		super();
		this.studentId = studentId;
		this.name = name;
		this.score = score;
		this.level = level;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
}
