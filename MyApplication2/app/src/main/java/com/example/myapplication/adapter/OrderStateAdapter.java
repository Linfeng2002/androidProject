package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.myapplication.R;

import java.util.List;

public class OrderStateAdapter extends BaseAdapter {
    private final List<String> list;
    private final  Context context;
    public OrderStateAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }
    private int selectedPosition = -1;
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
        view= LayoutInflater.from(context).inflate(R.layout.order_state_button,null);
        Button button=view.findViewById(R.id.order_state);

        button.setTextColor(Color.BLACK);
        Drawable drawable=null;
        switch (list.get(i)) {
            case "全部订单":
                button.setText("全部订单");
                drawable = AppCompatResources.getDrawable(context, R.drawable.ic_all_order);
                break;
            case "待付款":
                button.setText("待付款");
                drawable = AppCompatResources.getDrawable(context, R.drawable.ic_unpay);
                break;
            case "待出行":
                button.setText("待出行");
                drawable = AppCompatResources.getDrawable(context, R.drawable.ic_unjourney);
                break;
            case "待评价":
                button.setText("待评价");
                drawable = AppCompatResources.getDrawable(context, R.drawable.ic_uncommend);
                break;
        }
        button.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
        if(i==selectedPosition) button.setTextColor(Color.BLUE);
        else button.setTextColor(Color.BLACK);

        return button;
    }
    public void setSelectedPosition(int position) { selectedPosition = position; notifyDataSetChanged(); }
}
