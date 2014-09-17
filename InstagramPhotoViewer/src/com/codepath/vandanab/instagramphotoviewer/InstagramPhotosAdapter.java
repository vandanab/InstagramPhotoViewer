package com.codepath.vandanab.instagramphotoviewer;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.mikhaellopez.circularimageview.CircularImageView;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

	// View lookup cache.
	private static class ViewHolder {
		ImageView photo;
		CircularImageView profilePic;
		TextView username;
		TextView caption;
		TextView likesCount;
		TextView comment;
	}

	public InstagramPhotosAdapter(Context context, ArrayList<InstagramPhoto> photos) {
		super(context, R.layout.item_photo_1, photos);
	}

	// if the layout resource is a textview which generally calls the toString of the model.
	// in this case the model is InstagramPhoto, so its toString method will be invoked.

	// Take the data item at a position, converts it to a row in the listview.
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item.
		InstagramPhoto photo = getItem(position);

		ViewHolder viewHolder; // view lookup cache stored in cache.

		// Check if we are using a recycled view.
		if (convertView == null) { // if we are not using the a recycled view,
			// we need to create the view from scratch.
			// load the view from a particular xml template.
			// params of inflate: xml we want to inflate, container we want to attach to, don't attach yet.

			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.item_photo_1, parent, false);
			viewHolder.photo = (ImageView) convertView.findViewById(R.id.imgPhoto);
			viewHolder.profilePic = (CircularImageView) convertView.findViewById(R.id.imgProfilePic);
			viewHolder.caption = (TextView) convertView.findViewById(R.id.tvCaption);
			viewHolder.username = (TextView) convertView.findViewById(R.id.tvUsername);
			viewHolder.likesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
			viewHolder.comment = (TextView) convertView.findViewById(R.id.tvComment);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		/* Commenting to implement using viewHolder pattern.
		// Lookup the subview within the template.
		ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
		//ImageView imgProfilePic = (ImageView) convertView.findViewById(R.id.imgProfilePic);
		CircularImageView imgProfilePic = (CircularImageView) convertView.findViewById(R.id.imgProfilePic);
		TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
		TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
		TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
		TextView tvComment = (TextView) convertView.findViewById(R.id.tvComment);*/

		// Populate the subviews for that data item (testview, imageview, etc. with the correct data).
		viewHolder.profilePic.setImageResource(0);
		Picasso.with(getContext()).load(photo.profileImageUrl).into(viewHolder.profilePic);

		viewHolder.username.setText(photo.username);
		viewHolder.caption.setText(Html.fromHtml("<font color=\"#015994\"><b>" + photo.username +
				"</b></font> -- " + photo.caption));
		viewHolder.photo.getLayoutParams().height = photo.imageHeight;
		// Reset the image from the recycled view.
		viewHolder.photo.setImageResource(0);
		// Ask for the photo to be added to the imageview based on the photo url.
		// Send network request to the url, download the image bytes,
		// convert to bitmap image, resizing the image, insert bitmap in imageview.
		// We want all this to happen on a different background thread.
		// Picasso does all this and more (cache images, etc.) for us with just one statement.
		Picasso.with(getContext()).load(photo.imageUrl).into(viewHolder.photo);
		viewHolder.likesCount.setText(photo.likesCount + " likes");
		if (photo.comments.size() > 0) {
			viewHolder.comment.setVisibility(View.VISIBLE);
			InstagramPhoto.Comment comment = photo.comments.get(0); 
			viewHolder.comment.setText(Html.fromHtml("<font color=\"#015994\"><b>" + comment.username + "</font></b> " + comment.text));
		} else {
			viewHolder.comment.setVisibility(View.INVISIBLE);
		}

		// Return the view for that data item
		return convertView;
	}
	
}
