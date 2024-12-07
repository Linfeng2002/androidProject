package com.example.myapplication;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.example.myapplication.data.model.AppDatabase;
import com.example.myapplication.entity.Article;
import com.example.myapplication.entity.PersonalInformation;
import com.example.myapplication.entity.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainApplication extends Application {
    private static MainApplication mApp;

    private AppDatabase appDatabase;

    public PersonalInformation personalInformations=new PersonalInformation();
    public Map<String,String> tokenMap=new HashMap<>();
    public List<Article> articleList=Article.getDefault();
    /**
     * 单例模式获取唯一实例
     * @return
     */
    public static MainApplication getInstance(){
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp=this;
        appDatabase= Room.databaseBuilder(this, AppDatabase.class, "PersonalInformation")
                .fallbackToDestructiveMigration()
                .build();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        this.getArticle();

        //首先查找用户登录，未找到则代表没登录
        int userId = sharedPreferences.getInt("userId", -1);
        if(userId>0){//找到了代表已登录，从本地数据库中获取数据赋值到全局变量中
            new Thread(() -> personalInformations=getPersonalDatabase().personalInformationDao().getUserById(userId)).start();

        }else personalInformations=PersonalInformation.getDefaultPersonalInformation();//赋一个默认值，防止空指针报错

    }

    public AppDatabase getPersonalDatabase(){
        return appDatabase;
    }

    /**
     * app销毁时调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        //将全局变量用户信息的id传入SharedPreferences，再写入数据库
        SharedPreferences.Editor myPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
        myPrefs.putInt("userId",personalInformations.getUserId());
        myPrefs.apply();
        //写入数据库
        getPersonalDatabase().personalInformationDao().insert(personalInformations);

    }

    public void getArticle(){
        new Thread(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            //首先查找用户登录，未找到则代表没登录
            int userId = sharedPreferences.getInt("userId", -1);
            OkHttpClient okHttpClient = new OkHttpClient();
            Gson gson = new Gson();
            Request build = new Request.Builder().url("http://192.168.1.7:8083/androidArticle/getRecommendArticle?userId=0" ).build();
            okHttpClient.newCall(build).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String string = response.body().string();
                    Type resultType = new TypeToken<Result<List<Article>>>() {}.getType();
                    Result<List<Article>> result = gson.fromJson(string, resultType);
                    if(result.getCode()==200){
                        List<Article> temp=(List<Article>)result.getData();
                        articleList.clear();
                        articleList.addAll(temp);
                    }
                }
            });
        }).start();
    }

    public void loginResult(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);  // 设置为已登录
        editor.putInt("userId", Integer.parseInt(tokenMap.get("id")));//设置登录的id
        editor.apply();

    }
}
