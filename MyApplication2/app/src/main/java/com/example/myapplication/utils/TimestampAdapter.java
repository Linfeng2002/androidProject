package com.example.myapplication.utils;

import com.google.gson.JsonDeserializer;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {

    // 定义目标日期格式
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @Override
    public JsonElement serialize(Timestamp src, Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
        return new JsonPrimitive(dateFormat.format(src));
    }

    @Override
    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String dateStr = json.getAsString();
        try {
            Date date = dateFormat.parse(dateStr);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            throw new JsonParseException("Failed to parse Date value: " + dateStr, e);
        }
    }
}


