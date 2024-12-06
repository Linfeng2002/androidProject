package com.example.myapplication.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.entity.PersonalInformation;

@Dao
public interface PersonalInformationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PersonalInformation personalInformation);

    @Query("SELECT * FROM PersonalInformation WHERE userId = :userId")
    PersonalInformation getUserById(int userId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(PersonalInformation personalInformation);

    @Delete
    void delete(PersonalInformation personalInformation);
}

