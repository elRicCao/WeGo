package vn.edu.hcmut.wego.activity;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.server.AuthenticationService;
import vn.edu.hcmut.wego.server.AuthenticationService.SignUpResult;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends ActionBarActivity {

	private EditText usernameField;
	private EditText emailField;
	private EditText passwordField;
	private EditText phoneField;
	private Button signupButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		getControl();
		addAction();
	}

	// Binding views and objects
	private void getControl() {
		usernameField = (EditText) findViewById(R.id.activity_signup_username);

		emailField = (EditText) findViewById(R.id.activity_signup_email);

		passwordField = (EditText) findViewById(R.id.activity_signup_password);
		passwordField.setTypeface(Typeface.SANS_SERIF);

		phoneField = (EditText) findViewById(R.id.activity_signup_phone);

		signupButton = (Button) findViewById(R.id.activity_signup_button);

	}

	// Add event handler to views
	private void addAction() {

		// Sign up button: click event
		signupButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				// Get fields content
				String username = usernameField.getText().toString();
				String email = emailField.getText().toString();
				String password = passwordField.getText().toString();
				String phone = phoneField.getText().toString();

				// Use service to send info to server and receive result
				SignUpResult result = AuthenticationService.submitSignupInformation(username, email, password, phone);

				// Switch case result: EXIST_EMAIL, EXIST_PHONE and SUCCESS
				// Show dialog for each case
				switch (result) {
				case EXIST_EMAIL: {
					AlertDialog.Builder builder = new Builder(SignUpActivity.this);
					builder.setTitle("Sign up Failed").setMessage("Email is already exist.");
					builder.create();
					builder.show();
					break;
				}

				case EXIST_PHONE: {
					AlertDialog.Builder builder = new Builder(SignUpActivity.this);
					builder.setTitle("Sign up Failed").setMessage("Phone is already exist.");
					builder.create();
					builder.show();
					break;
				}

				// Case Success
				default: {
					AlertDialog.Builder builder = new Builder(SignUpActivity.this);
					builder.setTitle("Success").setMessage("Your account has been successfully registered.");
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Return to login activity
							finish();
						}
					});
					builder.create();
					builder.show();
					break;
				}
				}
			}
		});
	}

}
