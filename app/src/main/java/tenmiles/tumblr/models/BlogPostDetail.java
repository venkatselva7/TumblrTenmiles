package tenmiles.tumblr.models;

import java.io.Serializable;

/**
 * Created by Venkatesh on 14-11-2016.
 * venkatselva8@gmail.com
 */

public class BlogPostDetail{

    public String postTitle;

    public String blogName;

    public String postBody;

    public String postDate;

    public String blogAvatar;

    public String getBlogAvatar() {
        return blogAvatar;
    }

    public void setBlogAvatar(String blogAvatar) {
        this.blogAvatar = blogAvatar;
    }

    public String getPostBody() {
        return postBody;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }
}
