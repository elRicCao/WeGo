package vn.edu.hcmut.wego.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Place;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class DrawPath {
	private GoogleMap map;
	private ArrayList<LatLng> markerPoints;
	private ArrayList<Place> places;
	private Context context;
	private LatLng origin, dest;
	private Polyline polyline;
	private PolylineOptions polyOption;
	private ArrayList<Marker> markerList;
	private ArrayList<MarkerOptions> markerOptionList;
	private DrawCallback callback;
	
	public Polyline getPolyline(){
		return polyline;
	}
	
	public ArrayList<LatLng> getMarkerPoints(){
		return markerPoints;
	}
	
	public ArrayList<MarkerOptions> getMarkerOptionList(){
		return markerOptionList;
	}

	public PolylineOptions getPolyOption()
	{
		return polyOption;
	}
	public DrawPath(GoogleMap map, Context context, ArrayList<Place> places) {
		this.map = map;
		this.context = context;
		this.places = places;
		markerPoints = new ArrayList<LatLng>();
		for (int i = 0; i < places.size(); i++) {
			markerPoints.add(new LatLng(places.get(i).getLatitude(), places.get(i).getLongitude()));
		}
		this.origin = markerPoints.get(0);
		this.dest = markerPoints.get(markerPoints.size() - 1);
		this.markerList = new ArrayList<Marker>();
		this.markerOptionList = new ArrayList<MarkerOptions>();
	}

	public void startDraw(DrawCallback callback) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		Marker marker;
		this.callback = callback;
		MarkerOptions markerOption;
		for(int i = 0; i < places.size(); i++)
		{	
		//	Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ho_chi_minh, options);
			markerOption = new MarkerOptions().position(markerPoints.get(i)).title(places.get(i).getName());
			marker = map.addMarker(markerOption);
			markerList.add(marker);
			markerOptionList.add(markerOption);
		}

		if (markerPoints.size() >= 2) {
			origin = markerPoints.get(0);
			dest = markerPoints.get(markerPoints.size() - 1);
			// Getting URL to the Google Directions API
			String url = getDirectionsUrl(origin, dest);

			DownloadTask downloadTask = new DownloadTask();

			// Start downloading json data from Google Directions API
			downloadTask.execute(url);
		}
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, HashMap<String, List<List<HashMap<String, String>>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected HashMap<String, List<List<HashMap<String, String>>>> doInBackground(String... jsonData) {

			JSONObject jObject;
			HashMap<String, List<List<HashMap<String, String>>>> result = new HashMap<String, List<List<HashMap<String, String>>>>();

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				result = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(HashMap<String, List<List<HashMap<String, String>>>> result) {

			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();
				// Fetching i-th route
				List<HashMap<String, String>> path = result.get("routes").get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(2);
				lineOptions.color(Color.RED);
			}
			// Drawing polyline in the Google Map for the i-th route
			polyOption = lineOptions.width(10).color(context.getResources().getColor(R.color.green)).geodesic(true);
			polyline = map.addPolyline(lineOptions.width(10).color(context.getResources().getColor(R.color.green)).geodesic(true));
			if (origin.latitude <= dest.latitude && origin.longitude <= dest.longitude)
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(origin, dest), 0));
			else if (origin.latitude <= dest.latitude && origin.longitude >= dest.longitude)
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(origin.latitude, dest.longitude), new LatLng(dest.latitude, origin.longitude)), 0));
			else if (origin.latitude >= dest.latitude && origin.longitude <= dest.longitude)
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(dest.latitude, origin.longitude), new LatLng(origin.latitude, dest.longitude)), 0));
			else
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(dest.latitude, dest.longitude), new LatLng(origin.latitude, origin.longitude)), 0));
			map.animateCamera(CameraUpdateFactory.zoomOut());
			map.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(final Marker marker) {
					final Handler handler = new Handler();

					final long startTime = SystemClock.uptimeMillis();
					final long duration = 2000;

					Projection proj = map.getProjection();
					final LatLng markerLatLng = marker.getPosition();
					Point startPoint = proj.toScreenLocation(markerLatLng);
					startPoint.offset(0, -100);
					final LatLng startLatLng = proj.fromScreenLocation(startPoint);

					final Interpolator interpolator = new BounceInterpolator();

					handler.post(new Runnable() {
						@Override
						public void run() {
							long elapsed = SystemClock.uptimeMillis() - startTime;
							float t = interpolator.getInterpolation((float) elapsed / duration);
							double lng = t * markerLatLng.longitude + (1 - t) * startLatLng.longitude;
							double lat = t * markerLatLng.latitude + (1 - t) * startLatLng.latitude;
							marker.setPosition(new LatLng(lat, lng));

							if (t < 1.0) {
								// Post again 16ms later.
								handler.postDelayed(this, 16);
							}
						}
					});
					return false;
				}

			});
			if (callback != null) {
				callback.onDoneDrawing();
			}
		}
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Waypoints
		String waypoints = "";
		for (int i = 1; i < markerPoints.size() - 1; i++) {
			LatLng point = (LatLng) markerPoints.get(i);
			if (i == 1)
				waypoints = "waypoints=";
			waypoints += point.latitude + "," + point.longitude + "|";
		}

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
		return url;
	}
	
	public interface DrawCallback {
		public void onDoneDrawing();
	}

}
