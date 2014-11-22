package vn.edu.hcmut.wego.entity;

import java.util.Date;

/**
 * General type used for user-to-user message, group-message, trip-message, comment and super class of Review
 * 
 * @author elRic
 *
 */
public class Message {

	// Sender of this message
	private User sender;

	// Content of this message
	private String content;

	// Sending time of this message
	private Date time;

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
