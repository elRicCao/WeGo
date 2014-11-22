package vn.edu.hcmut.wego.entity;

/**
 * Entity for storing cost information
 * 
 * @author elRic
 *
 */
public class Cost {

	private int accommodation;
	private int transport;
	private int food;
	private int activity;
	private int others;
	private int total;

	public Cost() {

	}

	public Cost(int accomodation, int transport, int food, int activity, int others) {
		this.accommodation = accomodation;
		this.transport = transport;
		this.food = food;
		this.activity = activity;
		this.others = others;
		this.total = accomodation + transport + food + activity + others;
	}

	public int getAccommodation() {
		return accommodation;
	}

	public void setAccommodation(int accommodation) {
		this.accommodation = accommodation;
	}

	public int getTransport() {
		return transport;
	}

	public void setTransport(int transport) {
		this.transport = transport;
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}

	public int getActivity() {
		return activity;
	}

	public void setActivity(int activity) {
		this.activity = activity;
	}

	public int getOthers() {
		return others;
	}

	public void setOthers(int others) {
		this.others = others;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "Cost [accommodation=" + accommodation + ", transport=" + transport + ", food=" + food + ", activity=" + activity + ", others=" + others + "]";
	}

}
