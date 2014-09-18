package vn.edu.hcmut.wego.activity;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.service.Service;
import vn.edu.hcmut.wego.utility.Security;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {

	private EditText emailField;
	private EditText passwordField;
	private Button loginButton;
	private TextView forgotPasswordLink;
	private TextView signUppLink;
	private String email;
	private String encryptedPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getControl();
		addAction();
		if (loadLoginInfo()) {
			loginSequence();
		}
	}

	/**
	 * Binding control
	 */
	private void getControl() {
		emailField = (EditText) findViewById(R.id.login_email_field);
		passwordField = (EditText) findViewById(R.id.login_password_field);
		passwordField.setTypeface(Typeface.DEFAULT);
		loginButton = (Button) findViewById(R.id.login_button);
		forgotPasswordLink = (TextView) findViewById(R.id.login_forget_password_link);
		signUppLink = (TextView) findViewById(R.id.login_sign_up_link);
	}

	/**
	 * Add event handler to objects
	 */
	private void addAction() {

		// Login button clicked
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				email = emailField.getText().toString();
				encryptedPassword = Security.encrypt(passwordField.getText().toString());

				if (email.isEmpty()) {
					Toast.makeText(LoginActivity.this, "Please enter your email for your WeGo account", Toast.LENGTH_SHORT).show();
					emailField.requestFocus();
				} else if (encryptedPassword.isEmpty()) {
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
	 * @param email
	 * @param encryptedPassword
	 */
	private void loginSequence() {
		// Check Internet connection
		if (Service.checkInternetConnection(LoginActivity.this)) {

			// Authenticate login information
			if (Service.loginAuthentication(email, encryptedPassword, LoginActivity.this)) {
				// Store login information to preferences, so user don't need to login manual next time
				storeLoginInfo();
				
				// Proceed to main activity and finish this activity
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				showDialog("Login Failed", "Incorrect email or password. Please check your login information and try again.");
				passwordField.getText().clear();
				emailField.requestFocus();
			}

		} else {
			showDialog("Login Failed", "Sorry, login failed to reach WeGo servers. Please check your network connection or try again later.");
			passwordField.getText().clear();
			passwordField.requestFocus();
		}
	}
	
	/**
	 * Store user login information for next time
	 * @param email
	 * @param encryptedPassword
	 */
	private void storeLoginInfo() {
		SharedPreferences preferences = getSharedPreferences(Constant.PREFS_NAME, 0);

		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putString(Constant.USER_EMAIL, email);
		editor.putString(Constant.USER_ENCRYPTED_PASSWORD, encryptedPassword);
		
		editor.commit();
	}
	
	/**
	 * Load login information from preference
	 * @return true if login information exists, otherwise return false
	 */
	private boolean loadLoginInfo() {
		SharedPreferences preferences = getSharedPreferences(Constant.PREFS_NAME, 0);
		email = preferences.getString(Constant.USER_EMAIL, null);
		encryptedPassword = preferences.getString(Constant.USER_ENCRYPTED_PASSWORD, null);
		return (email != null) && (encryptedPassword != null);
	}

	/**
	 * Show dialog with given title and message
	 * @param title
	 * @param message
	 */
	private void showDialog(String title, String message) {
		AlertDialog.Builder builder = new Builder(LoginActivity.this);
		builder.setTitle(title).setMessage(message);
		builder.create();
		builder.show();
	}
}
