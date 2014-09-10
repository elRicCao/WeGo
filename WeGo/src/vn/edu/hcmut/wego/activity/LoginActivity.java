package vn.edu.hcmut.wego.activity;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.logic.LoginLogic;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getControl();
		addAction();
	}

	// Binding views and objects
	private void getControl() {
		emailField = (EditText) findViewById(R.id.login_email_field);

		passwordField = (EditText) findViewById(R.id.login_password_field);
		passwordField.setTypeface(Typeface.DEFAULT);

		loginButton = (Button) findViewById(R.id.login_button);
		
		forgotPasswordLink = (TextView) findViewById(R.id.login_forget_password_link);
		
		signUppLink = (TextView) findViewById(R.id.login_sign_up_link);
	}

	// Add event handler to views
	private void addAction() {
		
		// Login button clicked
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				String email = emailField.getText().toString();
				String password = passwordField.getText().toString();

				if (email.isEmpty()) {
					Toast.makeText(LoginActivity.this, "Please enter your email for your WeGo account", Toast.LENGTH_SHORT).show();
					emailField.requestFocus();
				} else if (password.isEmpty()) {
					Toast.makeText(LoginActivity.this, "Please enter your WeGo password", Toast.LENGTH_SHORT).show();
					passwordField.requestFocus();
				} else {
					// Check Internet connection
					if (LoginLogic.checkInternetConnection(LoginActivity.this)) {

						// Authenticate login information
						if (LoginLogic.loginAuthentication(email, password)) {
							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							startActivity(intent);
							finish();
						} else {
							AlertDialog.Builder builder = new Builder(LoginActivity.this);
							builder.setTitle("Login Failed").setMessage("Incorrect email or password. Please check your login information and try again.");
							builder.create();
							builder.show();
						}

					} else {
						AlertDialog.Builder builder = new Builder(LoginActivity.this);
						builder.setTitle("Login Failed").setMessage("Sorry, login failed to reach WeGo servers. Please check your network connection or try again later.");
						builder.create();
						builder.show();
					}
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

}
