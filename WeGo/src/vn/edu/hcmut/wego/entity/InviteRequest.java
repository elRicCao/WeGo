package vn.edu.hcmut.wego.entity;

/**
 * General class for invite and request
 * @author elRic
 *
 */
public class InviteRequest {

	// Type of invite or request
	public enum Type {
		FRIEND_REQUEST, GROUP_REQUEST, GROUP_INVITE, TRIP_REQUEST, TRIP_INVITE
	};
	
	private Type type;

	// Id of invite or request corresponding to type
	private int id;
	
	// Id of sender corresponding to type
	private int senderId;
	
	// Id of receiver corresponding to type
	private int receiverId;
	

	public InviteRequest() {
	}

	public InviteRequest(int id, int senderId, int receiverId, Type type) {
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
