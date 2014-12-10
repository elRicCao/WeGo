package vn.edu.hcmut.wego.activity;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import vn.edu.hcmut.wego.utility.Security;
import vn.edu.hcmut.wego.utility.Utils;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.widget.LoginButton;

public class LoginActivity extends ActionBarActivity {

	// Dialog title and content
	private static final String DIALOG_TITLE_LOGIN_FAILED = "Login Failed";
	private static final String INVALID_LOGIN_INFO = "Incorrect email or password. Please check your login information and try again.";
	private static final String NO_CONNECTION = "Sorry, login failed to reach WeGo servers. Please check your network connection or try again later.";

	// Views
	private EditText emailField;
	private EditText passwordField;
	private Button loginButton;
	private TextView signUpLink;
	private ProgressBar progressBar;
	private LoginButton fbLoginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (isLoggedIn()) {
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

		// Set up input fields
		emailField = (EditText) findViewById(R.id.activity_login_email);
		passwordField = (EditText) findViewById(R.id.activity_login_password);
		passwordField.setTypeface(Typeface.SANS_SERIF);

		// Set up login button
		loginButton = (Button) findViewById(R.id.activity_login_button);
		loginButton.setOnClickListener(new LoginListener());

		// Set up sign up link
		signUpLink = (TextView) findViewById(R.id.activity_login_sign_up);
		signUpLink.setOnClickListener(new SignUpListener());

		// Set up progress Bar
		progressBar = (ProgressBar) findViewById(R.id.activity_login_progress_bar);

		// Set up Facebook login button
		fbLoginButton = (LoginButton) findViewById(R.id.activity_login_facebook_button);
		// fbLoginButton.setReadPermissions(Arrays.asList("user_likes", "user_status"));
		fbLoginButton.setSessionStatusCallback(new FacebookStatusCallBack());
		fbLoginButton.setOnClickListener(new FacebookLoginListener());
	}

	@Override
	protected void onPause() {
		super.onPause();
		AppEventsLogger.deactivateApp(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	/**
	 * On Click Listener for Login button
	 */
	private class LoginListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			// Hide soft keyboard
			InputMethodManager manager = (InputMethodManager) LoginActivity.this.getSystemService(Service.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			
			if (emailField.getText().toString().trim().isEmpty()) {
				Toast.makeText(LoginActivity.this, "Please enter your email for your WeGo account", Toast.LENGTH_SHORT).show();
				emailField.requestFocus();
				return;
			}
			if (passwordField.getText().toString().trim().isEmpty()) {
				Toast.makeText(LoginActivity.this, "Please enter your WeGo password", Toast.LENGTH_SHORT).show();
				passwordField.requestFocus();
				return;
			}
			
			onLogin();
		}
	}

	/**
	 * On Click Listener for Sign up link
	 */
	private class SignUpListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * Status Callback for Facebook session
	 */
	private class FacebookStatusCallBack implements StatusCallback {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			if (exception != null) {
				if (exception instanceof FacebookOperationCanceledException) {
					showActionViews();
					return;
				}
			}
			if (session.isOpened()) {
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		}
	}

	/**
	 * On Click Listener for Facebook login button
	 */
	private class FacebookLoginListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			hideActionViews();
		}
	}

	/**
	 * Handle login sequence: check internet connection, create {@link ServerRequest} to send and receive data from server
	 */
	private void onLogin() {
		// Check Internet connection
		if (!Utils.checkInternetConnection(this)) {
			// Show alert dialog: no connection
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle(DIALOG_TITLE_LOGIN_FAILED).setMessage(NO_CONNECTION).create().show();
			return;
		}

		// Get email and password from fields
		String email = emailField.getText().toString().trim();
		String encryptedPassword = Security.encrypt(passwordField.getText().toString().trim());

		// Set progress bar visible
		progressBar.setVisibility(View.VISIBLE);
		
		// Hide other action views
		hideActionViews();

		// Make params for request
		JSONObject params = new JSONObject();
		try {
			params.put("email", email);
			params.put("password", encryptedPassword);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Create a new Login ServerRequest and execute
		ServerRequest.newServerRequest(RequestType.LOGIN, params, new ServerRequestCallback() {

			@Override
			public void onCompleted(Object... results) {
				// Hide progress bar
				progressBar.setVisibility(View.GONE);
			
				if (results.length == 0 || results[0] == null) {
					// Show alert dialog: invalid login info
					AlertDialog.Builder builder = new Builder(LoginActivity.this);
					builder.setTitle(DIALOG_TITLE_LOGIN_FAILED).setMessage(INVALID_LOGIN_INFO).create().show();
					
					// Clear fields and request focus
					passwordField.getText().clear();
					emailField.requestFocus();
					
					// Show action views
					showActionViews();
					return;
				}
				// Save login info
				User user = (User) results[0];
				
				// Proceed to main activity and finish this activity
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				intent.putExtra(Constant.INTENT_USER_ID, user.getId());
				startActivity(intent);
				finish();
			}

		}).executeAsync();
	}

	/**
	 * Check whether user has logged in
	 * @return true if there is preference logged in data, otherwise return false
	 */
	private boolean isLoggedIn() {
		Session session = Session.getActiveSession();
		if (session != null) {
			if (!session.isClosed())
				if (!session.getAccessToken().isEmpty())
					return true;
		}
		return false;
		//TODO: Debug
//		return false;
	}

	/**
	 * Hide all action views when user click login button
	 */
	private void hideActionViews() {
		emailField.setVisibility(View.GONE);
		passwordField.setVisibility(View.GONE);
		loginButton.setVisibility(View.GONE);
		fbLoginButton.setVisibility(View.GONE);
		signUpLink.setVisibility(View.GONE);
	}

	/**
	 * Show all action views when login failed
	 */
	private void showActionViews() {
		emailField.setVisibility(View.VISIBLE);
		passwordField.setVisibility(View.VISIBLE);
		loginButton.setVisibility(View.VISIBLE);
		fbLoginButton.setVisibility(View.VISIBLE);
		signUpLink.setVisibility(View.VISIBLE);
	}
}
