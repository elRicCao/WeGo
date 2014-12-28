package vn.edu.hcmut.wego.entity;

import java.util.ArrayList;
import java.util.Date;

public class News {

	public enum NewsType {
		POST, REVIEW, PHOTO, COMMENT_POST, LIKE_POST, LIKE_REVIEW, COMMENT_PHOTO, LIKE_PHOTO
	}

	private int id;
	private ArrayList<User> actors;
	private User owner;
	private NewsType type;
	private String content;
	private String photo;
	private Place place;
	private Date time;
	private int numOfLikes;
	private int numOfComments;
	private int rate;
	
	public News() {
		// TODO Auto-generated constructor stub
		actors = new ArrayList<User>();
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<User> getActors() {
		return actors;
	}

	public void setActors(ArrayList<User> actors) {
		this.actors = actors;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public NewsType getType() {
		return type;
	}

	public void setType(NewsType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getNumOfLikes() {
		return numOfLikes;
	}

	public void setNumOfLikes(int numOfLikes) {
		this.numOfLikes = numOfLikes;
	}

	public int getNumOfComments() {
		return numOfComments;
	}

	public void setNumOfComments(int numOfComments) {
		this.numOfComments = numOfComments;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}
	public void addActor(User user)
	{
		actors.add(user);
	}

}
