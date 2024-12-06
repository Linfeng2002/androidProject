package com.example.myapplication.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.entity.Result;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Glide.with(this).load("/storage/emulated/0/Android/data/com.example.myapplication/files/Download/微信图片_20241118164436.jpg")
                .into(binding.loginBackground);

        loginViewModel = new ViewModelProvider(this)
                .get(LoginViewModel.class);
        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        final EditText authCode=binding.authCode;
        final Button getAuthCode=binding.getAuthCode;
        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
            if(loginFormState.getAuthCodeError()!=null){
                authCode.setError(getString(loginFormState.getAuthCodeError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                Toast.makeText(LoginActivity.this,loginResult.getError(),Toast.LENGTH_SHORT).show();
                return;
            }
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
            }
            setResult(Activity.RESULT_OK);


            finish();
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            /**
             * 监听输入框修改，修改后调用loginDataChanged判断是否合法
             */
            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),authCode.getText().toString()
                );
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            /**
             * 设置回车时提交
             */
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(),
                            authCode.getText().toString(),LoginActivity.this
                    );
                }
                return false;
            }
        });
        /**
         * 设置点击登录按钮时提交
         */
        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString(),
                    authCode.getText().toString(),LoginActivity.this
            );
        });
        getAuthCode.setOnClickListener(view -> {
            getAuthCode(usernameEditText.getText().toString());
        });
    }

    /**
     * 登录成功后设置状态为已登录
     */
    private void updateUiWithUser(Map<String,String> model) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);  // 设置为已登录
        editor.putString("userId",model.get("id"));//设置登录的id
        editor.apply();

    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public void getAuthCode(String username) {
        new Thread(() -> {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request build = new Request.Builder().url("http://10.44.174.235:8083/androidUser/getAuthCode?username="+username).get().build();
            Gson gson = new Gson();
            okHttpClient.newCall(build).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String string = response.body().string();
                    Result result = gson.fromJson(string, Result.class);
                    if(result.getCode()==200){
                        runOnUiThread(() -> binding.authCode.setText(result.getData().toString()));
                    }
                }
            });
        }).start();
    }
}