package vn.edu.hcmut.wego.logic;

public class SignUpLogic {
	
	// TODO: Add more result if needed
	/**
	 * Collection of possible result when signing up
	 * @author elRic
	 *
	 */
	public enum SignUpResult {SUCCESS, EXIST_EMAIL, EXIST_PHONE}

	// TODO: Send info to server and return result, remember to encode password before sent
	/**
	 * Send register information to server and receive result
	 * @param username
	 * @param email
	 * @param password
	 * @param phone
	 * @return SUCCESS if everything is fine, EXIST_EMAIL or EXIST_PHONE if email or phone has already been used
	 */
	public static SignUpResult submitSignupInformation(String username, String email, String password, String phone) {
		SignUpResult result = SignUpResult.SUCCESS;
		
		return result;
	}
}
