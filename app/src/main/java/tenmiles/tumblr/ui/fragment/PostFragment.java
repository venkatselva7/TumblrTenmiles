package tenmiles.tumblr.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.TextPost;
import com.tumblr.jumblr.types.User;

import java.util.ArrayList;
import java.util.List;

import tenmiles.tumblr.R;
import tenmiles.tumblr.adapter.BlogPostAdapter;
import tenmiles.tumblr.helpers.Config;
import tenmiles.tumblr.helpers.HideKeyBoard;
import tenmiles.tumblr.helpers.PrefUtil;
import tenmiles.tumblr.models.BlogPostDetail;
import tenmiles.tumblr.ui.communicator.FragmentIntermediateInterface;

/**
 * Created by Venkatesh on 14-11-2016.
 * venkatselva8@gmail.com
 */

public class PostFragment extends Fragment {
    PrefUtil prefUtil;
    Context context;
    JumblrClient client;
    User user;
    RecyclerView rvPostList;
    BlogPostAdapter blogPostAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<BlogPostDetail> outerListPostDetails = new ArrayList<>();
    Blog blog;
    Dialog addNewPostDialog;
    Animation shake;
    ProgressDialog progressDialog;
    FragmentIntermediateInterface fragmentIntermediateInterface;
    View rootView;
    boolean isFirstRun = true;
    private boolean isImageLoaded = true;

    public static PostFragment newInstance(String selectedBlogUrl) {
        PostFragment helpFragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Config.SELECTED_BLOG_URL, selectedBlogUrl);
        helpFragment.setArguments(bundle);
        return helpFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        fragmentIntermediateInterface = (FragmentIntermediateInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_post, container, false);
        context = getActivity();
        prefUtil = new PrefUtil(context);
        rvPostList = (RecyclerView) rootView.findViewById(R.id.rv_post_list);
        mLayoutManager = new LinearLayoutManager(context);
        rvPostList.setLayoutManager(mLayoutManager);
        rvPostList.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_add_post);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPost();
            }
        });

        shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        loadPost();
        return rootView;
    }


    private void addNewPost() {
        addNewPostDialog = new Dialog(context);
        addNewPostDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addNewPostDialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        addNewPostDialog.setContentView(R.layout.dialog_add_text_post);
        addNewPostDialog.show();
        Button btnCancel = (Button) addNewPostDialog.findViewById(R.id.btn_post_cancel);
        Button btnPostIt = (Button) addNewPostDialog.findViewById(R.id.btn_post);
        final EditText etPostTitle = (EditText) addNewPostDialog.findViewById(R.id.et_post_title);
        final EditText etPostBody = (EditText) addNewPostDialog.findViewById(R.id.et_post_body);

        addNewPostDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(context, "Post cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPostDialog.dismiss();
            }
        });
        btnPostIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etPostTitle.getText().length() == 0) {
                    showErrorEditText(etPostTitle);
                    return;
                }
                if (etPostBody.getText().length() == 0) {
                    showErrorEditText(etPostBody);
                    return;
                }
                new HideKeyBoard(getActivity()).hide();
                addNewPostDialog.dismiss();
                saveNewPost(etPostBody.getText().toString(), etPostTitle.getText().toString());
            }
        });

    }

    private void saveNewPost(String postBody, String postTitle) {
        try {
            TextPost addTextPost = client.newPost(blog.getName(), TextPost.class);
            addTextPost.setBody(postBody);
            addTextPost.setTitle(postTitle);
            addTextPost.save();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        loadPost();
    }

    private void showErrorEditText(EditText editText) {
        editText.startAnimation(shake);
        editText.setError("shouldn't be empty");
    }

    private void loadPost() {
        progressDialog = ProgressDialog.show(context, "Please wait", "Loading", true);
        new LoadPostTask().execute();
    }


    public void deletePost(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete the post ?");
        builder.setTitle("Delete Post");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                blog.posts().get(position).delete();
                loadPost();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public class LoadPostTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            if (isFirstRun) {
                client = new JumblrClient(Config.CONSUMER_KEY, Config.CONSUMER_SECRET);
                client.setToken(prefUtil.getStringPref(Config.TUMBLR_OAUTH_TOKEN, ""), prefUtil.getStringPref(Config.TUMBLR_OAUTH_TOKEN_SECRET, ""));
                user = client.user();
                blog = client.blogInfo(getArguments().getString(Config.SELECTED_BLOG_URL));
                isFirstRun = false;
            }
            ArrayList<BlogPostDetail> listPostDetails = new ArrayList<>();
            try {
                List<Post> listPost = blog.posts();
                for (int j = 0; j < listPost.size(); j++) {
                    Post post = listPost.get(j);
                    if (post.getType().equals("text")) {
                        TextPost textPost = (TextPost) post;
                        BlogPostDetail blogPostDetail = new BlogPostDetail();
                        blogPostDetail.setPostTitle(textPost.getTitle());
                        blogPostDetail.setBlogName(blog.getName());
                        if (isImageLoaded)
                            blogPostDetail.setBlogAvatar(blog.avatar());
                        blogPostDetail.setPostBody(textPost.getBody());
                        blogPostDetail.setPostDate(textPost.getDateGMT());
                        listPostDetails.add(blogPostDetail);
                    }
                }
                outerListPostDetails = listPostDetails;
                blogPostAdapter = new BlogPostAdapter(context, listPostDetails);
            } catch (Exception e) {
                isImageLoaded = false;
                return e.getMessage();
            }
            if (listPostDetails.size() > 0) {
                return "OK";
            } else {
                return "NO_POSTS";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (result.equals("OK")) {
                rvPostList.setAdapter(blogPostAdapter);
            } else if (result.equals("NO_POSTS")) {
                if (!isFirstRun) {
                    rvPostList.setAdapter(blogPostAdapter);
                }
                Toast.makeText(context, "No posts", Toast.LENGTH_SHORT).show();
            } else {
                loadPost();
                //  Toast.makeText(context, " Error " + result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
