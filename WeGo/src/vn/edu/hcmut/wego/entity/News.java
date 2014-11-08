package vn.edu.hcmut.wego.entity;

import java.util.ArrayList;
import java.util.Date;

public class News {

	public enum NewsType {
		POST, REVIEW, PHOTO, COMMENT_POST, LIKE_POST, COMMENT_REVIEW, LIKE_REVIEW, COMMENT_PHOTO, LIKE_PHOTO
	}

	private int id;
	private ArrayList<User> actors;
	private User owner;
	private String content;
	private String photo;
	private Date time;
	private int numOfLikes;
	private int numOfComments;

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

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
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

}
