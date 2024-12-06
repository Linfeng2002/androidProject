package com.example.myapplication.Fragment.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.myapplication.ArticleDetailActivity;
import com.example.myapplication.MainApplication;
import com.example.myapplication.R;
import com.example.myapplication.adapter.ActionSelectAdapter;
import com.example.myapplication.adapter.ArticleAdapter;
import com.example.myapplication.article_search;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.entity.PersonalInformation;
import com.example.myapplication.entity.actionSelect;
import com.example.myapplication.entity.Article;
import com.example.myapplication.entity.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    List<Article> articleList =new ArrayList<>();
    ArticleAdapter articleAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        //设置功能选项，机票，火车票，酒店，攻略4种选项
        GridView gridView=binding.actionSelect;
        List<actionSelect> list = actionSelect.getDefault();
        ActionSelectAdapter adapter = new ActionSelectAdapter(getContext(), list);
        gridView.setAdapter(adapter);
        //点击跳转的逻辑
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
        });


        GridView gridView1=binding.placeRecommend;
         articleList = MainApplication.getInstance().articleList;
         articleAdapter = new ArticleAdapter(getContext(), articleList);
        gridView1.setAdapter(articleAdapter);
        gridView1.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
            intent.putExtra("index",position);
            //intent.putExtra("article", articleList.get(position));
            startActivity(intent);
        });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //首先查找用户登录，未找到则代表没登录
        int userId = sharedPreferences.getInt("userId", -1);
        binding.topSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                new Thread(() -> {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Gson gson = new Gson();
                    Request build = new Request.Builder().url("http://10.44.174.235:8083/androidArticle/getSearchArticle?searchMessage=" + s + "&userId=" + userId).build();
                    try {
                        Response execute = okHttpClient.newCall(build).execute();
                        Type resultType = new TypeToken<Result<List<Article>>>() {}.getType();
                        Result<List<Article>> result = gson.fromJson(execute.body().string(), resultType);
                        if(result.getCode()==200){
                            List<Article> articles=(List<Article>)result.getData();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {//跳转到搜索页面
                                    Intent intent = new Intent(getActivity(), article_search.class);
                                    intent.putExtra("articles", (Serializable) articles);
                                    startActivity(intent);
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainApplication.getInstance().getArticle();
        articleAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}