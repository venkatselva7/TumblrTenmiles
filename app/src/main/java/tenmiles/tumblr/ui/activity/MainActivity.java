package tenmiles.tumblr.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.User;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import tenmiles.tumblr.R;
import tenmiles.tumblr.helpers.Config;
import tenmiles.tumblr.helpers.PrefUtil;
import tenmiles.tumblr.models.UserBlog;
import tenmiles.tumblr.ui.communicator.DeletePostInterface;
import tenmiles.tumblr.ui.communicator.FragmentIntermediateInterface;
import tenmiles.tumblr.ui.fragment.PostFragment;
import tenmiles.tumblr.ui.fragment.BlogFragment;

/**
 * Created by Venkatesh on 20/9/16.
 * venkatselva8@gmail.com
 */

public class MainActivity extends AppCompatActivity implements FragmentIntermediateInterface, DeletePostInterface {

    Toolbar toolbar;
    PrefUtil prefUtil;
    JumblrClient client;
    User user;
    Context context;
    ProgressDialog progressDialog;
    ArrayList<UserBlog> outerListUserBlog;
    PostFragment postFragment;
    String userName;
    boolean isImageLoaded = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefUtil = new PrefUtil(context);
        client = new JumblrClient(Config.CONSUMER_KEY, Config.CONSUMER_SECRET);
        client.setToken(prefUtil.getStringPref(Config.TUMBLR_OAUTH_TOKEN, ""), prefUtil.getStringPref(Config.TUMBLR_OAUTH_TOKEN_SECRET, ""));
        user = client.user();
        userName = StringUtils.capitalize(user.getName());
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getUsersBlogs();
    }


    private void setFragmentLayout(Fragment fragmentName, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragmentName.isAdded()) {
            fragmentTransaction.remove(fragmentName);
        }
        fragmentTransaction.replace(R.id.frag_container, fragmentName, tag);
        fragmentTransaction.commit();
    }

    @Override
    public void setBlogListFragmentPage(ArrayList<UserBlog> outerListUserBlog) {
        getSupportActionBar().setTitle(userName + "'s " + Config.BLOGS);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Config.CURRENT_FRAGMENT = Config.BLOG_FRAGMENT;
        setFragmentLayout(new BlogFragment().newInstance(outerListUserBlog), Config.BLOGS);
    }

    @Override
    public void setPostListFragmentPage(String blogUrl, String blogName) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        getSupportActionBar().setTitle(StringUtils.capitalize(blogName) + "'s " + Config.POSTS);
        Config.CURRENT_FRAGMENT = Config.POST_FRAGMENT;
        setFragmentLayout(new PostFragment().newInstance(blogUrl), Config.POSTS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        switch (Config.CURRENT_FRAGMENT) {
            case Config.BLOG_FRAGMENT:
                super.onBackPressed();
                break;

            case Config.POST_FRAGMENT:
                setBlogListFragmentPage(outerListUserBlog);
                break;
        }
    }

    public void getUsersBlogs() {
        progressDialog = ProgressDialog.show(context, "Please wait", "Loading", true);
        new LoadBlogTask().execute();
    }

    @Override
    public void deletePost(int position) {
        postFragment = (PostFragment) getSupportFragmentManager().findFragmentByTag(Config.POSTS);
        postFragment.deletePost(position);
    }

    public class LoadBlogTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            ArrayList<UserBlog> listUserBlog = new ArrayList<>();
            UserBlog userBlog;
            try {
                for (int i = 0; i < user.getBlogs().size(); i++) {
                    userBlog = new UserBlog();
                    if (isImageLoaded) {
                        userBlog.setBlogAvatar(user.getBlogs().get(i).avatar());
                    }
                    userBlog.setBlogName(user.getBlogs().get(i).getName());
                    userBlog.setBlogUrl(user.getBlogs().get(i).getName() + ".tumblr.com");
                    userBlog.setBlogTitle(user.getBlogs().get(i).getTitle());
                    listUserBlog.add(userBlog);
                }
                outerListUserBlog = listUserBlog;
            } catch (Exception e) {
                isImageLoaded = false;
                return e.getMessage();
            }

            if (listUserBlog.size() > 0) {
                return "OK";
            } else {
                return "NO_BLOGS";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result.equals("OK")) {
                setBlogListFragmentPage(outerListUserBlog);
            } else if (result.equals("NO_BLOGS")) {
                Toast.makeText(context, "some", Toast.LENGTH_SHORT).show();
            } else {
                getUsersBlogs();
               // Toast.makeText(context, " Error " + result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_logout:
                userLogoutDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void userLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to logout and exit the app ?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logout();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void logout() {

        prefUtil.clearAllSharedPreferences();
        finish();
    }
}