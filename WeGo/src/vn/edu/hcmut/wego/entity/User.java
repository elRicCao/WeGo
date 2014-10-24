package vn.edu.hcmut.wego.entity;

public class User {

	public enum UserType {
		NORMAL, FRIEND, FOLLOW
	};

	private int id;
	private String name;
	private String email;
	private String phone;
	private String status;

	public User() {
	}

	public User(int id, String name, String email, String phone, String status) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.status = status;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
