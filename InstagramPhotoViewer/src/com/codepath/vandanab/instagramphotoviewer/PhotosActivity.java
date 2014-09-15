package com.codepath.vandanab.instagramphotoviewer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class PhotosActivity extends Activity {
	private static final String CLIENT_ID = "90d06d6b5e9c404a8c87f1e885849e6d";
	private static final String URL = "https://api.instagram.com/v1/media/popular?client_id=";
	private ArrayList<InstagramPhoto> photos;
	private InstagramPhotosAdapter photosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        // Send the request and download the data from internet.
        fetchPopularPhotos();
    }

	private void fetchPopularPhotos() {
		// Create data source.
		photos = new ArrayList<InstagramPhoto>();
		// Create the adapter and bind it to the data source.
		photosAdapter = new InstagramPhotosAdapter(this, photos);
		// Populate the data into the listview.
		ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
		lvPhotos.setAdapter(photosAdapter); // this triggers actual population of items.
		
		// Setup popular url endpoint.
		String popularUrl = URL + CLIENT_ID;
		// Create the network client.
		AsyncHttpClient client = new AsyncHttpClient();
		// Trigger the network request.
		client.get(popularUrl, new JsonHttpResponseHandler() {

			// define success and failure callbacks.
			// Handle successful response (popular photos json).

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// fired once the successful response comes back.
				// response object == popular photos json
				JSONArray photosJSON = null;
				try {
					photos.clear();
					photosJSON = response.getJSONArray("data");
					for (int i = 0; i < photosJSON.length(); i++) {
						JSONObject photoJSON = photosJSON.getJSONObject(i);
						InstagramPhoto photo = new InstagramPhoto();
						photo.username = photoJSON.getJSONObject("user").getString("username");
						photo.profileImageUrl = photoJSON.getJSONObject("user").getString("profile_picture");
						if (photoJSON.getJSONObject("caption") != null) {
							photo.caption = photoJSON.getJSONObject("caption").getString("text");
						}
						photo.imageUrl = photoJSON.getJSONObject("images")
								.getJSONObject("standard_resolution").getString("url");
						photo.imageHeight = photoJSON.getJSONObject("images")
								.getJSONObject("standard_resolution").getInt("height");
						photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
						if (photoJSON.getJSONObject("comments") != null) {
							JSONArray commentsJSON = photoJSON.getJSONObject("comments").getJSONArray("data");
							photo.comments = new ArrayList<InstagramPhoto.Comment>();
							for (int j = 0; j < commentsJSON.length() && j < 2; j++) {
								JSONObject commentJSON = commentsJSON.getJSONObject(j);
								InstagramPhoto.Comment comment = new InstagramPhoto.Comment(
										commentJSON.getString("text"),
										commentJSON.getJSONObject("from").getString("username"));
								photo.comments.add(comment);
							}
						}
						photos.add(photo);
					}
					// notify the adapter that it should populate new changes into the listview.
					photosAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// Fire if there is a problem in parsing the data JSONArray in the response.
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			
		});
		
		
	}

    
}
