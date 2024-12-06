package com.example.myapplication.entity;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class actionSelect {
    public int icon;
    public String action;
    public int color;
    public actionSelect(int icon,String action,int color){
        this.icon=icon;
        this.action=action;
        this.color=color;
    }
    public static int[] iconArray={R.drawable.ic_plane,R.drawable.ic_train,R.drawable.ic_hotel,R.drawable.ic_strategy};

    public static String[] actionArray={"飞机","火车","酒店","攻略"};

    public static int[] colorArray={R.color.purple_200,R.color.purple_500,R.color.teal_200,R.color.teal_700};

    public static List<actionSelect> getDefault(){
        ArrayList<actionSelect> list = new ArrayList<>();
        for(int i=0;i<iconArray.length;i++){
            list.add(new actionSelect(iconArray[i],actionArray[i],colorArray[i] ));
        }
        return list;
    }
}
