package vn.edu.hcmut.wego.entity;

/**
 * General class for invite and request
 * 
 * @author elRic
 *
 */
public class InviteRequest {

	// Type of invite or request
	public enum Type {
		FRIEND_REQUEST, GROUP_REQUEST, GROUP_INVITE, TRIP_REQUEST, TRIP_INVITE
	};

	private Type type;

	// If type is FRIEND_REQUEST, GROUP_REQUEST, TRIP_REQUEST, sender is User
	// If type is GROUP_INVITE or TRIP_INVITE, sender is Group or Trip correspondingly
	private Object sender;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Object getSender() {
		return sender;
	}

	public void setSender(Object sender) {
		this.sender = sender;
	}

}
