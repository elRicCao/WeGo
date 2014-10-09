package vn.edu.hcmut.wego.entity;

public class Offer {

	public enum Type {
		FRIEND_REQUEST, GROUP_REQUEST, GROUP_INVITE
	};

	private int requesterId;

	public int getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(int requesterId) {
		this.requesterId = requesterId;
	}

}
