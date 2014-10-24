package vn.edu.hcmut.wego.entity;

public class Offer {

	public enum Type {
		FRIEND_REQUEST, GROUP_REQUEST, GROUP_INVITE
	};

	private int id;
	private int senderId;
	private int receiverId;
	private Type type;

	public Offer() {
	}

	public Offer(int id, int senderId, int receiverId, Type type) {
		this.id = id;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int sender) {
		this.senderId = sender;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiver) {
		this.receiverId = receiver;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
