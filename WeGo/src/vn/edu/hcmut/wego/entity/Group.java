package vn.edu.hcmut.wego.entity;

public class Group {

	private int id;
	private String name;
	private String description;

	// TODO: will change to enum type later
	private String status;

	public Group() {

	}

	public Group(int id, String name, String description, String status) {
		this.id = id;
		this.name = name;
		this.description = description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
