package vn.edu.hcmut.wego.activity;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.logic.UserLogic;
import vn.edu.hcmut.wego.utility.Security;
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
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {

	private EditText emailField;
	private EditText passwordField;
	private Button loginButton;

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
	}

	// Add event handler to views
	private void addAction() {
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
					if (UserLogic.checkInternetConnection()) {

						// Authenticate login information
						if (UserLogic.loginAuthentication(email, Security.encode(password))) {
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
	}

}
