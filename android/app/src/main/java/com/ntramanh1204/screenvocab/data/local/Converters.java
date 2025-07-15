// File: Converters.java
package com.ntramanh1204.screenvocab.data.local;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static String fromList(List<String> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    public static List<String> toList(String value) {
        Type type = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, type);
    }
}
