package tenmiles.tumblr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tenmiles.tumblr.R;
import tenmiles.tumblr.models.BlogPostDetail;
import tenmiles.tumblr.ui.communicator.DeletePostInterface;

/**
 * Created by Venkatesh on 14/11/16.
 * venkatselva8@gmail.com
 */

public class BlogPostAdapter extends RecyclerView.Adapter<BlogPostAdapter.ViewHolder> {
    Context context;
    ArrayList<BlogPostDetail> listBlogPost;
    DeletePostInterface deletePostInterface;

    public BlogPostAdapter(Context context, ArrayList<BlogPostDetail> listBlogPost) {
        this.context = context;
        this.listBlogPost = listBlogPost;
        deletePostInterface=(DeletePostInterface) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.indiv_post_list, null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvBlogName.setText(listBlogPost.get(position).getBlogName());
        holder.tvPostTitle.setText(listBlogPost.get(position).getPostTitle());
        Glide.with(context).load(listBlogPost.get(position).getBlogAvatar()).into(holder.imBlogAvatar);
        holder.tvPostBody.setText(Html.fromHtml(listBlogPost.get(position).getPostBody()));
        holder.tvPostDate.setText(listBlogPost.get(position).getPostDate());
        holder.imPostDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               deletePostInterface.deletePost(position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return listBlogPost.size();
    }

    @Override
    public int getItemCount() {
        return listBlogPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imBlogAvatar;
        TextView tvBlogName;
        TextView tvPostTitle;
        TextView tvPostBody;
        TextView tvPostDate;
        ImageView imPostDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            imBlogAvatar = (ImageView) itemView.findViewById(R.id.im_blog_avatar);
            tvBlogName = (TextView) itemView.findViewById(R.id.tv_blog_name);
            tvPostTitle = (TextView) itemView.findViewById(R.id.tv_post_title);
            tvPostBody = (TextView) itemView.findViewById(R.id.tv_post_body);
            tvPostDate = (TextView) itemView.findViewById(R.id.tv_blog_date);
            imPostDelete = (ImageView) itemView.findViewById(R.id.im_post_delete);
        }
    }

}
