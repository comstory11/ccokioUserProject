package domain;

public class NonUser {

	private String nonUserName;
	private String nonUserTel;
	private String nonUserAddress;
	
	public NonUser() {

	}

	public NonUser(String nonUserName, String nonUserTel, String nonUserAddress) {
		this.nonUserName = nonUserName;
		this.nonUserTel = nonUserTel;
		this.nonUserAddress = nonUserAddress;
	}
	
	public NonUser(String nonUserName, String nonUserTel) {
		this.nonUserName = nonUserName;
		this.nonUserTel = nonUserTel;
	}

	public String getNonUserName() {
		return nonUserName;
	}

	public String getNonUserAddress() {
		return nonUserAddress;
	}

	public void setNonUserAddress(String nonUserAddress) {
		this.nonUserAddress = nonUserAddress;
	}

	public void setNonUserName(String nonUserName) {
		this.nonUserName = nonUserName;
	}

	public String getNonUserTel() {
		return nonUserTel;
	}

	public void setNonUserTel(String nonUserTel) {
		this.nonUserTel = nonUserTel;
	}

}
