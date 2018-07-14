package com.codepathtraining.parstaham.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    // ...

    // Associate each comment with a user
    public void setOwner(ParseUser user) {
        put("owner", user);
    }

    // Get the user for this comment
    public ParseUser getOwner()  {
        return getParseUser("owner");
    }

    // Associate each comment with a post
    public void setCommentText(String text) {
        put("text", text);
    }

    public String getCommentText()  {
        return getString("text");
    }

    // Associate each comment with a post
    public void setPost(Post post) {
        put("post", post);
    }

    // Get the post for this item
    public Post getPost()  {
        return (Post) getParseObject("post");
    }
}
