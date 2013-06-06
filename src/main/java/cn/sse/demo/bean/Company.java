package cn.sse.demo.bean;

public class Company {
	private int id;
	private String province;
	private String city;
	private int headCount;
	public Company(int id,String province, String city, int headCount) {
		super();
		this.id=id;
		this.province = province;
		this.city = city;
		this.headCount = headCount;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getHeadCount() {
		return headCount;
	}
	public void setHeadCount(int headCount) {
		this.headCount = headCount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Company [id=" + id + ", province=" + province + ", city="
				+ city + ", headCount=" + headCount + "]";
	}
	
	
	
}
