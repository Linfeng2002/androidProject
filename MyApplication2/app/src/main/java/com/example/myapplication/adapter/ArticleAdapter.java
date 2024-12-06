package com.example.myapplication.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.entity.Article;

import java.util.List;

public class ArticleAdapter extends BaseAdapter {
    private final Context context;
    private final List<Article> list;

    public ArticleAdapter(Context context, List<Article> list) {
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
            view=LayoutInflater.from(context).inflate(R.layout.article_item, null);
            viewHolder= new ViewHolder();
            viewHolder.articleImage=view.findViewById(R.id.article_image_pager);
            viewHolder.articleTitle=view.findViewById(R.id.article_title);
            viewHolder.articleAuthor=view.findViewById(R.id.article_author_name);
            viewHolder.articleVisitNum=view.findViewById(R.id.article_visit_num);
            viewHolder.authorImage=view.findViewById(R.id.article_author_image);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        Article article = list.get(i);
        //将图片的翻页适配器配置进去
        Glide.with(context).load(article.picture.get(0)).into(viewHolder.articleImage);
        viewHolder.articleTitle.setText(article.title);
        viewHolder.articleAuthor.setText(article.authorName);
        viewHolder.articleVisitNum.setText(String.valueOf(article.visitNum));
        Glide.with(context).load(article.authorImage).apply(RequestOptions.circleCropTransform()).into(viewHolder.authorImage);



        return view;
    }

    public static final class ViewHolder {
        public ImageView articleImage;
        public TextView articleTitle;
        public TextView articleAuthor;
        public TextView articleVisitNum;
        public ImageView authorImage;
    }
}
