package tenmiles.tumblr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tenmiles.tumblr.R;
import tenmiles.tumblr.models.UserBlog;

/**
 * Created by Venkatesh on 14/11/16.
 * venkatselva8@gmail.com
 */

public class UserBlogAdapter extends RecyclerView.Adapter<UserBlogAdapter.ViewHolder> {
    Context context;
    ArrayList<UserBlog> listUserBlog;

    public UserBlogAdapter(Context context, ArrayList<UserBlog> listUserBlog) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.listUserBlog = listUserBlog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.indiv_blog_list, null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvBlogName.setText(listUserBlog.get(position).getBlogName());
        holder.tvBlogTitle.setText(listUserBlog.get(position).getBlogTitle());
        Glide.with(context).load(listUserBlog.get(position).blogAvatar).into(holder.imBlogAvatar);

    }

    @Override
    public long getItemId(int position) {
        return listUserBlog.size();
    }

    @Override
    public int getItemCount() {
        return listUserBlog.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imBlogAvatar;
        TextView tvBlogName;
        TextView tvBlogTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            imBlogAvatar = (ImageView) itemView.findViewById(R.id.im_blog_avatar);
            tvBlogName = (TextView) itemView.findViewById(R.id.tv_blog_name);
            tvBlogTitle = (TextView) itemView.findViewById(R.id.tv_blog_title);
        }
    }

}
