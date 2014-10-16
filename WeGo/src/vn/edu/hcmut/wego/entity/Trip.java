package vn.edu.hcmut.wego.entity;

import java.util.ArrayList;

public class Trip {
	private int id;
	private Location start;
	private Location end;
	private String description;
	private ArrayList<User> members;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Location getStart() {
		return start;
	}

	public void setStart(Location start) {
		this.start = start;
	}

	public Location getEnd() {
		return end;
	}

	public void setEnd(Location end) {
		this.end = end;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<User> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<User> members) {
		this.members = members;
	}
}
