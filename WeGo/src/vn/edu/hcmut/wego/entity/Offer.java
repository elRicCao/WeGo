package vn.edu.hcmut.wego.entity;

public class Offer {

	public enum Type {
		FRIEND_REQUEST, GROUP_REQUEST, GROUP_INVITE
	};

	private int id;
	private int offererId;
	private Type type;
	
	public Offer(int id, int offererId, Type type) {
		this.id = id;
		this.offererId = offererId;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOffererId() {
		return offererId;
	}

	public void setOffererId(int offererId) {
		this.offererId = offererId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
