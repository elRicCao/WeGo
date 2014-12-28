package vn.edu.hcmut.wego.utility;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.graphics.Bitmap;

public class UploadImage {
	public static void DoUploadImage(Bitmap image, String name) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byte_arr = stream.toByteArray();
		String image_str = Base64.encodeBytes(byte_arr);
		final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("image", image_str));
		nameValuePairs.add(new BasicNameValuePair("name", name));

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost("http://wego.journeymanager.esy.es/img/upload_image.php");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
}