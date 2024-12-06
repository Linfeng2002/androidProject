package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.ViewGroup;

import android.widget.GridView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;


import com.example.myapplication.adapter.ArticleAdapter;
import com.example.myapplication.databinding.ActivityArticleSearchBinding;
import com.example.myapplication.entity.Article;
import com.example.myapplication.entity.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class article_search extends AppCompatActivity {
    ActivityArticleSearchBinding binding;
    List<Article> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityArticleSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        list=(List<Article>) getIntent().getSerializableExtra("articles");
        GridView listView=binding.searchResult;
        ArticleAdapter articleAdapter = new ArticleAdapter(article_search.this, list);
        listView.setAdapter(articleAdapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(article_search.this, ArticleDetailActivity.class);
            intent.putExtra("article", list.get(i));
            startActivity(intent);
        });
        Toolbar toolbar=binding.articleSearchToolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> finish());
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //首先查找用户登录，未找到则代表没登录
        toolbar.setTitle("搜索一下？");
        int userId = sharedPreferences.getInt("userId", -1);
        addMenuProvider(new MenuProvider() {//添加菜单配置
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_search,menu);
                MenuItem item = menu.findItem(R.id.action_search);
                SearchView actionView = (SearchView) item.getActionView();
                actionView.setIconifiedByDefault(false);
                actionView.setBackgroundResource(R.drawable.shape_edit_normal);
                actionView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                actionView.setQueryHint("想查点什么？");

                actionView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        //处理搜索提交
                        new Thread(() -> {
                            OkHttpClient okHttpClient = new OkHttpClient();
                            Gson gson = new Gson();
                            Request build = new Request.Builder().url("http://10.44.174.235:8083/androidArticle/getSearchArticle?searchMessage=" + query + "&userId=" + userId).build();
                            try {
                                Response execute = okHttpClient.newCall(build).execute();
                                Type resultType = new TypeToken<Result<List<Article>>>() {}.getType();
                                Result<List<Article>> result = gson.fromJson(execute.body().string(), resultType);
                                if(result.getCode()==200){
                                    List<Article> articles=(List<Article>)result.getData();
                                    runOnUiThread(() -> {//更新搜索界面
                                        list.clear();
                                        list.addAll(articles);
                                        articleAdapter.notifyDataSetChanged();
                                    });
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }).start();
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        //实时搜索处理
                        return false;
                    }
                });

            }
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId()==android.R.id.home){
                    finish();
                    return true;
                }
                return article_search.super.onOptionsItemSelected(menuItem);
            }
        },this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}