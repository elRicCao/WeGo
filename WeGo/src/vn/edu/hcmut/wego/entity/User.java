package vn.edu.hcmut.wego.entity;

import java.util.ArrayList;

public class User {

	private int id;
	private String name;
	private String image;
	private String email;
	private String phone;
	private String status;
	private Place location;
	private int numOfVote;
	private double averageVote;
	private ArrayList<Review> votes;

	public int getNumOfVote() {
		return numOfVote;
	}

	public void setNumOfVote(int numOfVote) {
		this.numOfVote = numOfVote;
	}

	public double getAverageVote() {
		return averageVote;
	}

	public void setAverageVote(double averageVote) {
		this.averageVote = averageVote;
	}

	public ArrayList<Review> getVotes() {
		return votes;
	}

	public void setVotes(ArrayList<Review> votes) {
		this.votes = votes;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public Place getLocation() {
		return location;
	}

	public void setLocation(Place location) {
		this.location = location;
	}
}
