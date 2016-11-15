package tenmiles.tumblr.models;

import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * Created by Venkatesh on 14-11-2016.
 * venkatselva8@gmail.com
 */

public class UserBlog implements Serializable {

    public String blogName;

    public String blogAvatar;

    public String blogTitle;

    public String blogUrl;

    public String getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    public String getBlogAvatar() {
        return blogAvatar;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogAvatar(String blogAvatar) {
        this.blogAvatar = blogAvatar;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }
}
