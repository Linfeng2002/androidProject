package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.adapter.ArticleAdapter;
import com.example.myapplication.databinding.ActivityBloggerPageBinding;
import com.example.myapplication.entity.PersonalInformation;
import com.example.myapplication.ui.login.LoginActivity;
import com.google.android.material.appbar.AppBarLayout;

public class bloggerPage extends AppCompatActivity {

    private OnBackPressedDispatcher onBackPressedDispatcher;
    ActivityBloggerPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBloggerPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button button=binding.bloggerMessageFollow;
        //获取当前博主信息
        PersonalInformation blogger=(PersonalInformation)getIntent().getSerializableExtra("blogger");
        if(blogger==null){
            Toast.makeText(this,"未找到该博主",Toast.LENGTH_SHORT).show();
            finish();
        }
        PersonalInformation personalInformations = MainApplication.getInstance().personalInformations;
        if (personalInformations!=null){//如果有登录并且已经关注，设置按钮为选中状态
            boolean isFollower=personalInformations.follower.contains(String.valueOf(blogger.userId));
            button.setSelected(isFollower);
        }

        binding.bloggerMessageFans.setText(getString(R.string.fans_number,blogger.fans.size()));
        binding.bloggerMessageFollowers.setText(getString(R.string.follower_number,blogger.follower.size()));
        binding.bloggerMessageName.setText(blogger.username);
        ArticleAdapter articleAdapter = new ArticleAdapter(this, blogger.userArticle);
        GridView articleView=binding.bloggerMessageArticle;
        articleView.setAdapter(articleAdapter);
        /**
         * 配置博主文章信息
         */
        articleView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(bloggerPage.this, ArticleDetailActivity.class);
            intent.putExtra("article", blogger.userArticle.get(i));
            startActivity(intent);
        });
        Glide.with(this).load(blogger.userPicture).into(binding.bloggerMessageImage);//配置头像

        button.setOnClickListener(view -> {//配置关注按钮的点击效果
            if(personalInformations!=null){
                if(button.isSelected()){//如果已选中
                    personalInformations.follower.remove(String.valueOf(blogger.userId));
                }else {
                    personalInformations.follower.add(String.valueOf(blogger.userId));
                }
                button.setSelected(!button.isSelected());
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(bloggerPage.this);
                builder.setTitle("您还未登录");
                builder.setMessage("是否选择去登录");
                builder.setPositiveButton("是", (dialogInterface, i) -> {
                    Intent intent = new Intent(bloggerPage.this, LoginActivity.class);
                    startActivity(intent);
                });
                builder.setNegativeButton("否", (dialogInterface, i) -> Toast.makeText(bloggerPage.this,"期待您的登录",Toast.LENGTH_SHORT).show());
                builder.create().show();
            }
        });
        Toolbar toolbar=binding.bloggerMessageToolbar;
        // 设置返回按钮的行为
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> finish());
        Glide.with(this).load("/storage/self/primary/Android/data/com.example.myapplication/files/Download/微信图片_20241118164436.jpg").into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                toolbar.setBackground(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
        //配置上滑时才显示导航栏背景
        binding.bloggerToolbar.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener) (appBarLayout, verticalOffset) -> {

            int totalScrollRange = appBarLayout.getTotalScrollRange();
            float alpha = 1 - (float) Math.abs(verticalOffset) / totalScrollRange;
            toolbar.setAlpha(alpha);
        });

    }
}