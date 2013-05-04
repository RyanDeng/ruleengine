package cn.dc.use;

public class UserAccount {
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 余额
	 */
	private Double balance;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
}
