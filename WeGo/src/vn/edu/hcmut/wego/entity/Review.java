package vn.edu.hcmut.wego.entity;

/**
 * General class for user-vote-user and user-review-place
 * @author elRic
 *
 */
public class Review extends Message {

	// Rate of this review, from 1 to 5
	private int rate;

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

}
