package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ArticleCommentBinding;
import com.example.myapplication.entity.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommentAdapter extends BaseAdapter {
    private List<Comment> firstList;
    private List<Comment> secondList;
    private Context context;

    public CommentAdapter(List<Comment> list, List<Comment> list1,Context context) {
        this.firstList = list;
        this.secondList=list1;
        this.context = context;
    }

    @Override
    public int getCount() {
        return firstList.size();
    }

    @Override
    public Object getItem(int i) {
        return firstList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
            if(view==null){
                view = LayoutInflater.from(context).inflate(R.layout.article_comment, null);
                viewHolder= new ViewHolder();
                viewHolder.commentContent=view.findViewById(R.id.article_comment_text);
                viewHolder.commentCreateTime=view.findViewById(R.id.article_comment_create_time);
                viewHolder.commentUserMessage=view.findViewById(R.id.article_comment_user_message);
                viewHolder.commentReply=view.findViewById(R.id.article_comment_reply);
                view.setTag(viewHolder);
            }else {
                viewHolder=(ViewHolder) view.getTag();
            }
            Comment comment=firstList.get(i);
            viewHolder.commentCreateTime.setText(String.valueOf(comment.createTime));
            viewHolder.commentUserMessage.setText(comment.username);
            Glide.with(context)
                    .load(comment.userImage)
                    .apply(RequestOptions.circleCropTransform()).override(100,100)
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
            viewHolder.commentReply.removeAllViews();
            //配置二级评论,如果该评论以及该评论下的二级评论不为空的话的话
        if(comment.id!=null){
            List<Comment> collect =secondList.stream().filter(comment1 -> comment1.parentCommentId.equals(comment.id)).collect(Collectors.toList());
            if(!collect.isEmpty()){
                for (Comment commentReply : collect) {
                    View viewChild= LayoutInflater.from(context).inflate(R.layout.article_comment_reply,viewGroup,false);
                    ViewHolderChild viewHolderChild=new ViewHolderChild();
                    viewHolderChild.commentContent=viewChild.findViewById(R.id.article_comment_reply_text);
                    viewHolderChild.commentCreateTime=viewChild.findViewById(R.id.article_comment_reply_create_time);
                    viewHolderChild.commentUserMessage=viewChild.findViewById(R.id.article_comment_reply_user_message);
                    viewChild.setTag(viewHolderChild);

                    viewHolderChild.commentCreateTime.setText(String.valueOf(commentReply.createTime) );
                    viewHolderChild.commentUserMessage.setText(commentReply.username);
                    Glide.with(context)
                            .load(commentReply.userImage)
                            .apply(RequestOptions.circleCropTransform()).override(70,70)
                            .into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    viewHolderChild.commentUserMessage.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null);
                                }
                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    // 清理资源时的逻辑
                                }
                            });
                    viewHolderChild.commentContent.setText(commentReply.content);


                    viewHolder.commentReply.addView(viewChild);
                }

            }
        }


        return view;
    }

    public final static class ViewHolder{
        public TextView commentUserMessage;
        public TextView commentContent;
        public TextView commentCreateTime;
        public LinearLayout commentReply;
    }
    private final static class ViewHolderChild{
        public TextView commentUserMessage;
        public TextView commentContent;
        public TextView commentCreateTime;
    }
}
