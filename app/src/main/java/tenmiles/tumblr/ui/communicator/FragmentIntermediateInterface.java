package tenmiles.tumblr.ui.communicator;

import java.util.ArrayList;

import tenmiles.tumblr.models.UserBlog;

/**
 * Created by Venkatesh on 15-11-2016.
 * venkatselva8@gmail.com
 */

public interface FragmentIntermediateInterface {
    public void setBlogListFragmentPage(ArrayList<UserBlog> outerListUserBlog);

    public void setPostListFragmentPage(String blogUrl,String blogName);
}
