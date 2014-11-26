package vn.edu.hcmut.wego.entity;

import java.util.ArrayList;

/**
 * Class for holding information of place. Place can be a administrative unit such as Province and City or detail unit such as Hotel and Restaurant
 * 
 * @author elRic
 *
 */
public class Place {

	public enum PlaceType {
		Province, City, Distric, MinorCity, Town, Hotel, Resort, RestHouse, Restaurant
	}

	// Id of place in database
	private int id;

	// Name of place
	private String name;

	// Description of place
	private String description;

	// Type of place
	private PlaceType type;

	// Total number of reviews of this place
	private int numOfReviews;

	// Average rate of this place
	private double averageRate;

	// Cost of place
	private int cost;

	// Exact address of place with number, street, ward
	private String address;

	// Place review
	private ArrayList<Review> reviews;

	// GPS location of place
	private double longtitude;
	private double latitude;

	// True if this place is a special landmark
	private boolean isSpecial;
	
	private Place district;
	private Place province;

	public Place getDistrict() {
		return district;
	}

	public void setDistrict(Place district) {
		this.district = district;
	}

	public Place getProvince() {
		return province;
	}

	public void setProvince(Place province) {
		this.province = province;
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

	public PlaceType getType() {
		return type;
	}

	public void setType(PlaceType type) {
		this.type = type;
	}

	public int getNumOfReviews() {
		return numOfReviews;
	}

	public void setNumOfReviews(int numOfReviews) {
		this.numOfReviews = numOfReviews;
	}

	public double getAverageRate() {
		return averageRate;
	}

	public void setAverageRate(double averageRate) {
		this.averageRate = averageRate;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public boolean isSpecial() {
		return isSpecial;
	}

	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}
}
