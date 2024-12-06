package com.example.myapplication.converter;

import androidx.room.TypeConverter;

import com.example.myapplication.entity.Article;
import com.example.myapplication.entity.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class personalConverter {

    @TypeConverter
    public static String fromListInteger(List<Integer> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    public static List<Integer> toListInteger(String value) {
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromListArticle(List<Article> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    public static List<Article> toListArticle(String value) {
        Type listType = new TypeToken<List<Article>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromListOrder(List<Order> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    public static List<Order> toListOrder(String value) {
        Type listType = new TypeToken<List<Order>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromListString(List<String> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    public static List<String> toListString(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
}

