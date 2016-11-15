package tenmiles.tumblr.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tenmiles.tumblr.R;
import tenmiles.tumblr.adapter.UserBlogAdapter;
import tenmiles.tumblr.helpers.RecyclerViewTouchListener;
import tenmiles.tumblr.models.UserBlog;
import tenmiles.tumblr.ui.communicator.FragmentIntermediateInterface;

import static tenmiles.tumblr.helpers.Config.ARRAY_BLOG_DETAILS;

/**
 * Created by Venkatesh on 14-11-2016.
 * venkatselva8@gmail.com
 */

public class BlogFragment extends Fragment {
    Context context;
    RecyclerView rvBlogList;
    UserBlogAdapter userBlogAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<UserBlog> outerListUserBlog = new ArrayList<>();
    View rootView;
    FragmentIntermediateInterface fragmentIntermediateInterface;

    public static BlogFragment newInstance(ArrayList<UserBlog> arrayUserBlog) {
        BlogFragment helpFragment = new BlogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARRAY_BLOG_DETAILS, arrayUserBlog);
        helpFragment.setArguments(bundle);
        return helpFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        fragmentIntermediateInterface = (FragmentIntermediateInterface) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_blog, container, false);
        context = getActivity();
        outerListUserBlog = (ArrayList<UserBlog>) getArguments().getSerializable(ARRAY_BLOG_DETAILS);
        rvBlogList = (RecyclerView) rootView.findViewById(R.id.rv_blog_list);
        mLayoutManager = new LinearLayoutManager(context);
        rvBlogList.setLayoutManager(mLayoutManager);
        userBlogAdapter = new UserBlogAdapter(context, outerListUserBlog);
        rvBlogList.setAdapter(userBlogAdapter);


        rvBlogList.addOnItemTouchListener(new RecyclerViewTouchListener(context, rvBlogList, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                fragmentIntermediateInterface.setPostListFragmentPage(outerListUserBlog.get(position).getBlogUrl(), outerListUserBlog.get(position).getBlogName());
            }
        }));
        return rootView;
    }


}
