package com.example.myapplication.data.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myapplication.Dao.PersonalInformationDao;
import com.example.myapplication.converter.personalConverter;
import com.example.myapplication.entity.PersonalInformation;

@Database(entities = {PersonalInformation.class}, version = 1, exportSchema = false)
@TypeConverters({personalConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonalInformationDao personalInformationDao();
}

