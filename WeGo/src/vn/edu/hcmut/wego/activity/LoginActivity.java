package vn.edu.hcmut.wego.activity;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.AuthenticationService;
import vn.edu.hcmut.wego.utility.Common;
import vn.edu.hcmut.wego.utility.Security;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {

	// Dialog title and content
	public static final String DIALOG_TITLE_LOGIN_FAILED = "Login Failed";
	public static final String DIALOG_CONTENT_INVALID_INFO = "Incorrect email or password. Please check your login information and try again.";
	public static final String DIALOG_CONTENT_NO_CONNECTION = "Sorry, login failed to reach WeGo servers. Please check your network connection or try again later.";
	
	// Views
	private EditText emailField;
	private EditText passwordField;
	private Button loginButton;
	private TextView forgotPasswordLink;
	private TextView signUppLink;

	// Login user
	private User loginUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set layout
		setContentView(R.layout.activity_login);

		// Get views in layout
		getView();

		// Set action for views
		setViewAction();

		// Check whether user info exists in database, if yes jump directly to main screen
		if (isLoginInfoExist()) {
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * Get views from layout
	 */
	private void getView() {
		// Fields
		emailField = (EditText) findViewById(R.id.login_email_field);
		passwordField = (EditText) findViewById(R.id.login_password_field);
		passwordField.setTypeface(Typeface.SANS_SERIF);

		// Button
		loginButton = (Button) findViewById(R.id.login_button);

		// Links
		forgotPasswordLink = (TextView) findViewById(R.id.login_forget_password_link);
		signUppLink = (TextView) findViewById(R.id.login_sign_up_link);
	}

	/**
	 * Set action of views
	 */
	private void setViewAction() {

		// Login button clicked
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (emailField.getText().toString().trim().isEmpty()) {
					Toast.makeText(LoginActivity.this, "Please enter your email for your WeGo account", Toast.LENGTH_SHORT).show();
					emailField.requestFocus();
				} else if (passwordField.getText().toString().trim().isEmpty()) {
					Toast.makeText(LoginActivity.this, "Please enter your WeGo password", Toast.LENGTH_SHORT).show();
					passwordField.requestFocus();
				} else {
					loginSequence();
				}
			}
		});

		// Forgot password link clicked
		forgotPasswordLink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Link to forget password activity
			}
		});

		// Sign up link clicked
		signUppLink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * Login sequence
	 * 
	 * @param email
	 * @param encryptedPassword
	 */
	private void loginSequence() {
		// Check Internet connection
		if (Common.checkInternetConnection(LoginActivity.this)) {

			// Authenticate login information
			loginUser = AuthenticationService.loginAuthentication(emailField.getText().toString().trim(), Security.encrypt(passwordField.getText().toString().trim()));
			
			if (loginUser != null) {
				// Store user information to preferences
				saveLoginInfo();

				// Proceed to main activity and finish this activity
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				// Show alert dialog: invalid login info
				AlertDialog.Builder builder = new Builder(LoginActivity.this);
				builder.setTitle(DIALOG_TITLE_LOGIN_FAILED).setMessage(DIALOG_CONTENT_INVALID_INFO).create().show();
			}

		} else {
			// Show alert dialog: no connection
			AlertDialog.Builder builder = new Builder(LoginActivity.this);
			builder.setTitle(DIALOG_TITLE_LOGIN_FAILED).setMessage(DIALOG_CONTENT_NO_CONNECTION).create().show();
		}

		// In case failed login, clear password field and return focus to email field
		passwordField.getText().clear();
		emailField.requestFocus();
	}

	/**
	 * Store user login information for next time
	 * 
	 * @param email
	 * @param encryptedPassword
	 */
	private void saveLoginInfo() {
		Common.putValueToSharedPreferences(this, Constant.PREFS_USER_ID, loginUser.getId());
		Common.putValueToSharedPreferences(this, Constant.PREFS_USER_EMAIL, loginUser.getEmail());
		Common.putValueToSharedPreferences(this, Constant.PREFS_USER_NAME, loginUser.getName());
	}

	/**
	 * Load login information from preference
	 * 
	 * @return true if login information exists, otherwise return false
	 */
	private boolean isLoginInfoExist() {
		int userId = (Integer) Common.getValueFromSharedPreferences(this, Constant.PREFS_USER_ID, Integer.class);
		return userId > 0;
	}
}
