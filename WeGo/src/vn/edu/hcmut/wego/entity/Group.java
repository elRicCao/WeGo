package vn.edu.hcmut.wego.entity;

import java.util.ArrayList;

/**
 * Class for holding group information
 * 
 * @author elRic
 *
 */
public class Group {

	// Id of group in database
	private int id;

	// Name of group
	private String name;

	// Description of group
	private String description;

	// Group message
	private ArrayList<Message> messages;

	// Group request
	private ArrayList<InviteRequest> requests;

	// Group invite
	private ArrayList<InviteRequest> invites;

	// Group privacy. If true, everone can see this group
	private boolean isPublic;
	
	private String announcement;

	public Group() {
		this.messages = new ArrayList<Message>();
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public ArrayList<InviteRequest> getRequests() {
		return requests;
	}

	public void setRequests(ArrayList<InviteRequest> requests) {
		this.requests = requests;
	}

	public ArrayList<InviteRequest> getInvites() {
		return invites;
	}

	public void setInvites(ArrayList<InviteRequest> invites) {
		this.invites = invites;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", description=" + description + ", isPublic=" + isPublic + "]";
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}

	public String getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}

}
