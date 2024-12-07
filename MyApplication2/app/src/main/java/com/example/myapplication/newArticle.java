package com.example.myapplication;

import static java.net.URI.create;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.documentfile.provider.DocumentFile;


import com.example.myapplication.adapter.ImagePagerAdapter;
import com.example.myapplication.databinding.ActivityNewArticleBinding;
import com.example.myapplication.entity.Article;
import com.example.myapplication.entity.NewArticle;
import com.example.myapplication.entity.PersonalInformation;
import com.example.myapplication.entity.Result;
import com.example.myapplication.utils.InputStreamRequestBody;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class newArticle extends AppCompatActivity {

    ActivityNewArticleBinding binding;
    ArrayList<Uri> stringList;
    ArrayList<String> Strings=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNewArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //获取图片信息
        stringList = getIntent().getParcelableArrayListExtra("stringList");
        for (Uri uri : stringList) {
            Strings.add(uri.toString());
        }
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(newArticle.this, Strings);
        binding.newArticlePicture.setAdapter(imagePagerAdapter);
        Toolbar toolbar=binding.newArticleToolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> finish());
        toolbar.setTitle("有什么好玩的要分享吗");
        //新建文章提交
        binding.newArticleSubmit.setOnClickListener(view -> new Thread(() -> {

            OkHttpClient okHttpClient = new OkHttpClient();
            MultipartBody.Builder builder = new MultipartBody.Builder() .setType(MultipartBody.FORM);
            Gson gson = new Gson();
            NewArticle article = new NewArticle();
            article.setArticleContent(String.valueOf(binding.newArticleContent.getText()));
            article.setArticleTitle(String.valueOf(binding.newArticleTitle.getText()));
            article.setAuthorUsername(MainApplication.getInstance().personalInformations.username);
            article.setAuthorId(MainApplication.getInstance().personalInformations.userId);
            builder.addFormDataPart("article", gson.toJson(article));

            for (Uri uri : stringList) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    if (inputStream != null) {
                        InputStreamRequestBody requestBody = new InputStreamRequestBody(MediaType.parse("image/jpeg"), inputStream);
                        builder.addFormDataPart("images", "image.jpg", requestBody);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            RequestBody requestBody = builder.build();
            Request build = new Request.Builder().url("http://192.168.1.7:8083/androidArticle/insertArticle").header("Authorization", MainApplication.getInstance().tokenMap.get("tokenHead") + " " + MainApplication.getInstance().tokenMap.get("token")).post(requestBody).build();
            okHttpClient.newCall(build).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        Type resultType = new TypeToken<Result<Article>>() {}.getType();
                        Result<PersonalInformation> result = gson.fromJson(response.body().string(), resultType);
                        runOnUiThread(() -> {
                            //执行成功后保存到本地
                            MainApplication.getInstance().personalInformations.userArticle.add((Article) result.getData());
                            Toast.makeText(newArticle.this,"发表成功",Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }


                }
            });
        }).start());

    }
    private String getFilePathFromUri(Uri uri) {
        String filePath = null;

        if ("content".equals(uri.getScheme())) {
            DocumentFile documentFile = DocumentFile.fromSingleUri(this, uri);
            if (documentFile != null && documentFile.exists()) {
                filePath = documentFile.getUri().getPath();
            }
        } else if ("file".equals(uri.getScheme())) {
            filePath = uri.getPath();
        }
        return filePath;
    }


}