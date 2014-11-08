package vn.edu.hcmut.wego.storage;

import java.util.ArrayList;

import vn.edu.hcmut.wego.entity.News;
import android.content.Context;
import android.os.AsyncTask;

public class LocalRequest {

	private AsyncTask<Void, Void, ArrayList<News>> newsFeedRequest;

	/**
	 * Private constructor used for creating request
	 * 
	 * @param newsFeedRequest
	 */
	private LocalRequest(AsyncTask<Void, Void, ArrayList<News>> newsFeedRequest) {
		this.newsFeedRequest = newsFeedRequest;
	}

	/**
	 * Execute an async task biding to this request object
	 */
	public void executeAsync() {
		if (newsFeedRequest != null)
			newsFeedRequest.execute();
	}

	/**
	 * Create a news feed request to database
	 * 
	 * @param context
	 * @param callBack
	 * @return LocalRequest which is ready to execute
	 */
	public static LocalRequest newsFeedRequest(Context context, NewsFeedCallBack callBack) {
		NewsFeedAsyncTask task = new NewsFeedAsyncTask(context, callBack);
		return new LocalRequest(task);
	}

	/**
	 * Async task used for retrieve news from database
	 */
	private static class NewsFeedAsyncTask extends AsyncTask<Void, Void, ArrayList<News>> {

		private NewsFeedCallBack callBack;
		private DatabaseOpenHelper database;

		public NewsFeedAsyncTask(Context context, NewsFeedCallBack callBack) {
			this.callBack = callBack;
			this.database = new DatabaseOpenHelper(context);
		}

		@Override
		protected ArrayList<News> doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<News> result) {
			callBack.onCompleted(result);
		}
	}

	/**
	 * Call back used for NewsFeedAsyncTask on post execute
	 */
	public interface NewsFeedCallBack {
		public void onCompleted(ArrayList<News> result);
	}
}
