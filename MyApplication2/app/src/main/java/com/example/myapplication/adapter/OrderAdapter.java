package com.example.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.entity.Order;

import java.util.List;

public class OrderAdapter extends BaseAdapter {
    private final Context context;
    private final List<Order> list;

    public OrderAdapter(Context context, List<Order> list) {
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

        return null;
    }

    public static final class ViewHolder {
        public ViewPager2 viewPager;
        public TextView articleTitle;
        public TextView articleAuthor;
        public TextView articleVisitNum;
        public ImageView authorImage;
    }
}
