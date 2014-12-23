package vn.edu.hcmut.wego.entity;

import java.util.ArrayList;

public class User{

	public enum UserStatus {
		OFFLINE, ONLINE
	}

	private int id;
	private String name;
	private String image;
	private String email;
	private String phone;
	private UserStatus status;
	private Place location;
	private int numOfVote;
	private int numOfFriend;
	private int numOfFollow;
	private int numOfTrip;
	private double averageVote;
	private ArrayList<Review> votes;
	private ArrayList<Message> recentMessages;
	private ArrayList<String> recentActivities;

	public User() {
		votes = new ArrayList<Review>();
		recentMessages = new ArrayList<Message>();
		recentActivities = new ArrayList<String>();
	}

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

	public int getNumOfFriend() {
		return numOfFriend;
	}

	public void setNumOfFriend(int numOfFriend) {
		this.numOfFriend = numOfFriend;
	}
	
	public int getNumOfFollow() {
		return numOfFollow;
	}

	public void setNumOfFollow(int numOfFollow) {
		this.numOfFollow = numOfFollow;
	}
	
	public int getNumOfTrip() {
		return numOfTrip;
	}

	public void setNumOfTrip(int numOfTrip) {
		this.numOfTrip = numOfTrip;
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

//	public UserStatus getStatus() {
//		return status;
//	}
//
//	public void setStatus(UserStatus status) {
//		this.status = status;
//	}

	public Place getLocation() {
		return location;
	}

	public void setLocation(Place location) {
		this.location = location;
	}

	public ArrayList<Message> getRecentMessages() {
		return recentMessages;
	}

	public void setRecentMessages(ArrayList<Message> recentMessages) {
		this.recentMessages = recentMessages;
	}

	public void addRecentMessage(Message message) {
		this.recentMessages.add(message);
	}
	
	public ArrayList<String> getRecentActivities() {
		return recentActivities;
	}
	
	public void setRecentActivities(ArrayList<String> recentActivities) {
		this.recentActivities = recentActivities;
	}

	public void addRecentActivity(String activity) {
		this.recentActivities.add(activity);
	}
}
