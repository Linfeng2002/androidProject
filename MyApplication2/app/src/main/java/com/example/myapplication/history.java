package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.adapter.ArticleAdapter;
import com.example.myapplication.databinding.ActivityHistoryBinding;
import com.example.myapplication.entity.Article;

import java.util.ArrayList;
import java.util.List;

public class history extends AppCompatActivity {
    private ActivityHistoryBinding binding;
    private OnBackPressedDispatcher onBackPressedDispatcher;
    List<Article> Articles =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {//判断传入的是游览历史还是收藏历史
            Articles = (List<Article>) bundle.getSerializable("recordArticle");
            if (Articles != null) {
                binding.historyToolbar.setTitle("游览历史");
            } else {
                Articles = (List<Article>) bundle.getSerializable("attentionArticle");
                binding.historyToolbar.setTitle("收藏历史");
            }
        }
        if(!Articles.isEmpty()){//配置适配器
            ArticleAdapter articleAdapter = new ArticleAdapter(this, Articles);
            binding.historyMessage.setAdapter(articleAdapter);
            List<Article> finalArticles = Articles;
            binding.historyMessage.setOnItemClickListener((adapterView, view, i, l) -> {

                Intent intent = new Intent(history.this, ArticleDetailActivity.class);
                intent.putExtra("article", finalArticles.get(i));
                startActivity(intent);
            });
        }else binding.historyToolbar.setSubtitle("暂无记录");



        setSupportActionBar(binding.historyToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        binding.historyToolbar.setNavigationOnClickListener(view -> finish());// 设置返回按钮的行为
        //配置搜索菜单



    }

}