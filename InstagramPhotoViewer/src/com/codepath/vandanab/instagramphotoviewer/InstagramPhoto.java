package com.codepath.vandanab.instagramphotoviewer;

import java.util.ArrayList;

public class InstagramPhoto {
	// username, caption, imageurl, likescount, comments, profileimage, comments
	public String username;
	public String profileImageUrl;
	public String caption;
	public String imageUrl;
	public String relativeTimeString;
	public int imageHeight;
	public int likesCount;
	public ArrayList<Comment> comments;
	
	static class Comment {
		public String text;
		public String username;
		public Comment(String commentText, String username) {
			this.text = commentText;
			this.username = username;
		}
	}

	/* not needed anymore.
	public String toString() {
		return "image - " + imageUrl;
	}*/
}
