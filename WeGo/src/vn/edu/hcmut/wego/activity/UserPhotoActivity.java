package vn.edu.hcmut.wego.activity;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.R.id;
import vn.edu.hcmut.wego.R.layout;
import vn.edu.hcmut.wego.R.menu;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class UserPhotoActivity extends ActionBarActivity {

	private Bitmap bitmap;
	private ImageView img;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_photo);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent i = getIntent();
		if(i!=null){
		bitmap = (Bitmap) i.getExtras().get("image");
		
		img = (ImageView) findViewById(R.id.imageView1);
		Matrix mat = new Matrix();
		mat.postRotate(90);
		Bitmap result = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),mat,true);
		img.setImageBitmap(result);
//		img.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Matrix mat = new Matrix();
//				mat.postRotate(90);
//				Bitmap result = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),mat,true);
//				img.setImageBitmap(result);
//			}
//		});
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.current_trip, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
