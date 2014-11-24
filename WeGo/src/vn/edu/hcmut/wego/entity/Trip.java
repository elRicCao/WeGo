package vn.edu.hcmut.wego.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Entity for storing trip information
 * 
 * @author elRic
 *
 */
public class Trip {

	// Id of trip in the database
	private int id;

	// Leader info of trip (user_id, user_name, user_reputation)
	private User leader;

	// Description of trip
	private String description;

	// Start day and end date of trip. Used to classify trip to Current, Coming or Finished
	private Date startDate;
	private Date endDate;

	// Current status of trip. Use predefined format to set
	private String status;

	// Start place of trip. This place must be administrative place such as City or Province
	private Place startPlace;

	// Route of trip. This byte array will be convert to collection of GPS location
	private byte[] route;

	// Plan of trip. This byte array will be convert to collection of Time + Activity
	private byte[] plan;

	// Cost of this trip. This byte array will be convert to Cost object
	private byte[] cost;

	// Privacy mode of trip. If this trip is public, everyone will be able to see and ask for permission to join trip.
	// Otherwise, the leader must manually invite members
	private boolean isPublic;

	// Collection of members of this trip
	private ArrayList<User> members;

	// Collection of places that this trip will visit. Chosen when creating trip
	private ArrayList<Place> places;

	// Trip message
	private ArrayList<Message> messages;

	// Trip request
	private ArrayList<InviteRequest> requests;

	// Trip invite
	private ArrayList<InviteRequest> invites;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getLeader() {
		return leader;
	}

	public void setLeader(User leader) {
		this.leader = leader;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Place getStartPlace() {
		return startPlace;
	}

	public void setStartPlace(Place startPlace) {
		this.startPlace = startPlace;
	}

	public byte[] getRoute() {
		return route;
	}

	public void setRoute(byte[] route) {
		this.route = route;
	}

	public byte[] getPlan() {
		return plan;
	}

	public void setPlan(byte[] plan) {
		this.plan = plan;
	}

	public byte[] getCost() {
		return cost;
	}

	public void setCost(byte[] cost) {
		this.cost = cost;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public ArrayList<User> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<User> members) {
		this.members = members;
	}

	public ArrayList<Place> getPlaces() {
		return places;
	}

	public void setPlaces(ArrayList<Place> places) {
		this.places = places;
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
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
		return "Trip [id=" + id + ", leader=" + leader + ", description=" + description + ", startDate=" + startDate + ", endDate=" + endDate + ", status=" + status + ", startPlace=" + startPlace
				+ ", route=" + Arrays.toString(route) + ", plan=" + Arrays.toString(plan) + ", isPublic=" + isPublic + ", members=" + members + ", places=" + places + ", cost=" + cost + "]";
	}

	/**
	 * Class for storing trip cost
	 * 
	 * @author elRic
	 *
	 */
	public static class Cost {
		public int accommodation;
		public int transport;
		public int food;
		public int activity;
		public int others;
		public int total;

		public Cost(int accomodation, int transport, int food, int activity, int others) {
			this.accommodation = accomodation;
			this.transport = transport;
			this.food = food;
			this.activity = activity;
			this.others = others;
			this.total = accomodation + transport + food + activity + others;
		}

		@Override
		public String toString() {
			return "Cost [accommodation=" + accommodation + ", transport=" + transport + ", food=" + food + ", activity=" + activity + ", others=" + others + "]";
		}
	}

}
