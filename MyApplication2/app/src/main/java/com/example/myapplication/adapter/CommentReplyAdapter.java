package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.R;
import com.example.myapplication.entity.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentReplyAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> list=new ArrayList<>();

    public CommentReplyAdapter(Context context, List<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
             view= LayoutInflater.from(context).inflate(R.layout.article_comment_reply,viewGroup,false);
            viewHolder=new ViewHolder();
            viewHolder.commentContent=view.findViewById(R.id.article_comment_reply_text);
            viewHolder.commentCreateTime=view.findViewById(R.id.article_comment_reply_create_time);
            viewHolder.commentUserMessage=view.findViewById(R.id.article_comment_reply_user_message);
            view.setTag(viewHolder);
        }else viewHolder=(ViewHolder) view.getTag();
        Comment comment=list.get(i);
        viewHolder.commentCreateTime.setText(new SimpleDateFormat("yyyy:MM:dd").format(new Date(comment.createTime.getTime())));
        viewHolder.commentUserMessage.setText(comment.username);
        Glide.with(context)
                .load(comment.userImage)
                .apply(RequestOptions.circleCropTransform()).override(70,70)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        viewHolder.commentUserMessage.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // 清理资源时的逻辑
                    }
                });
        viewHolder.commentContent.setText(comment.content);
        return view;
    }

    private final static class ViewHolder{
        public TextView commentUserMessage;
        public TextView commentContent;
        public TextView commentCreateTime;
    }
}
