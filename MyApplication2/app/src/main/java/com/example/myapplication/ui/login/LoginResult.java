package com.example.myapplication.ui.login;

import androidx.annotation.Nullable;

import com.example.myapplication.entity.PersonalInformation;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private Map<String,String> success;
    @Nullable
    private String error;

    LoginResult(@Nullable String error) {
        this.error = error;
    }

    LoginResult(@Nullable Map<String,String> success) {
        this.success = success;
    }

    @Nullable
    Map<String,String> getSuccess() {
        return success;
    }

    @Nullable
    String getError() {
        return error;
    }
}