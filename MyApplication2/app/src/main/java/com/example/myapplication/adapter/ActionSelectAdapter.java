package com.example.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import com.example.myapplication.entity.actionSelect;

import java.util.List;

public class ActionSelectAdapter extends BaseAdapter {

    private Context context;
    private List<actionSelect> list;

    public ActionSelectAdapter(Context context, List<actionSelect> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int i) {
        return this.list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Button button = new Button(this.context);
        actionSelect action= list.get(i);
        button.setText(action.action);
        button.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );
        button.setBackgroundColor(ContextCompat.getColor(this.context,action.color));
        button.setCompoundDrawablesWithIntrinsicBounds(0,action.icon,0,0);
        button.setPadding(10,10,10,10);
        button.setClickable(false);
        button.setFocusable(false);
        return button;
    }
}
