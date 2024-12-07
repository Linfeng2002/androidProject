package com.example.myapplication.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.os.Environment;
import android.util.Patterns;

import com.example.myapplication.Dao.PersonalInformationDao;
import com.example.myapplication.MainApplication;
import com.example.myapplication.R;
import com.example.myapplication.entity.Article;
import com.example.myapplication.entity.PersonalInformation;
import com.example.myapplication.entity.Result;
import com.example.myapplication.utils.ImageUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginViewModel extends ViewModel {

    private static MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private static MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    /**
     * 登录请求
     * @param username
     * @param password
     */
    public static void login(String username, String password, String authCode, Context context) {
        new Thread(() -> {
            OkHttpClient okHttpClient = new OkHttpClient();
            Gson gson = new Gson();
            FormBody builder = new FormBody.Builder()
                    .add("username",username)
                    .add("password",password)
                    .add("authCode",authCode)
                    .build();
            Request build = new Request.Builder()
                    .url("http://192.168.1.7:8083/androidUser/login")
                    .post(builder)
                    .build();
            okHttpClient.newCall(build).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    loginResult.setValue(new LoginResult(e.getMessage()) );
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String string = response.body().string();
                    Result result = gson.fromJson(string, Result.class);
                    if(result.getCode()!=200){//如果返回的结果不是正确的
                        loginResult.setValue(new LoginResult(result.getMessage()));
                    }else {

                        MainApplication.getInstance().tokenMap= (Map<String,String>) result.getData();
                        loginResult.postValue(new LoginResult(MainApplication.getInstance().tokenMap));
                        getUserById(context,MainApplication.getInstance().tokenMap.get("id"));
                        //设置状态为已登录
                        MainApplication.getInstance().loginResult();

                    }

                }
            });
        }).start();
    }

    public static void getUserById(Context context,String id){
        new Thread(() -> {
            OkHttpClient okHttpClient = new OkHttpClient();
            Gson gson = new Gson();
            Request build = new Request.Builder().url("http://192.168.1.7:8083/androidUser/getById?userId=" + MainApplication.getInstance().tokenMap.get("id"))
                    .header("Authorization", MainApplication.getInstance().tokenMap.get("tokenHead")+" " +MainApplication.getInstance().tokenMap.get("token")).build();
            try {
                Response execute = okHttpClient.newCall(build).execute();

                Type resultType = new TypeToken<Result<PersonalInformation>>() {}.getType();
                Result<PersonalInformation> result = gson.fromJson(execute.body().string(), resultType);
                if(result.getCode()==200){
                    PersonalInformation personalInformation=(PersonalInformation) result.getData();
                    MainApplication.getInstance().personalInformations=personalInformation;
                    savePersonalData(personalInformation,context);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * 用于将数据保存到本地，保存到数据库以及将图片缓存到本地
     * @param personalInformation 后端传递的用户信息
     */
    public static void savePersonalData(PersonalInformation personalInformation,Context context){
        //先将后端获取到的cdn路径下载下来，然后替换掉全局变量中的字符串路径，最后存储到本地数据库中
        //FileUtil是用于调用本地图片的，imageUtil用于下载网络上的图片文件
        PersonalInformationDao personalDao = MainApplication.getInstance().getPersonalDatabase().personalInformationDao();
        //获取本地用户信息，对比已有的本地图片资源决定是否下载
        PersonalInformation localPersonal = personalDao.getUserById(personalInformation.userId);

        for (Article article : personalInformation.userArticle) {//用户文章
            //本地数据库不存在后端的传递文章id，则需保存
            if(localPersonal!=null){
                if(localPersonal.userArticle.stream().noneMatch(article1 -> Objects.equals(article1.Id, article.Id))){
                    extracted(context, article);
                    localPersonal.userArticle.add(article);
                }
            }else extracted(context,article);

        }
        for (Article article : personalInformation.attentionArticle) {//收藏文章
            if (localPersonal!=null) {
                if(localPersonal.attentionArticle.stream().noneMatch(article1 -> Objects.equals(article1.Id, article.Id))){
                    extracted(context, article);
                    localPersonal.attentionArticle.add(article);
                }
            } else {extracted(context,article);
            }
        }
        for (Article article : personalInformation.recordArticle) {//游览历史文章
            if (localPersonal!=null) {
                if(localPersonal.recordArticle.stream().noneMatch(article1 -> Objects.equals(article1.Id, article.Id))){
                    extracted(context, article);
                    localPersonal.recordArticle.add(article);
                }
            } else {extracted(context,article);
            }
        }

        if(localPersonal==null) localPersonal=personalInformation;
        personalDao.insert(localPersonal);
        System.out.println("执行了插入本地数据库的操作");
    }

    private static void extracted(Context context, Article article) {
        List<String> list=ImageUtil.downloadAndSaveImage(context, article.picture);
        article.authorImage=ImageUtil.downloadAndSaveImage(context, article.authorImage, context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString()+"/"+System.currentTimeMillis()+".jpeg");
        int i=0;
        for (String s : list) {
            article.picture.set(i++,s);//替换原来的图片路径
        }
    }

    public void loginDataChanged(String username, String password,String authCode) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null,null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password,null));
        }
        else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    /**、
     * username 合法性检验
     * @param username
     * @return
     */
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.PHONE.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    /**
     * password检验
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }


}