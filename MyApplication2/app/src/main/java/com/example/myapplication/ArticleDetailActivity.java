package com.example.myapplication;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.adapter.CommentAdapter;
import com.example.myapplication.adapter.ImagePagerAdapter;
import com.example.myapplication.databinding.ArticleDetailBinding;

import com.example.myapplication.entity.Article;
import com.example.myapplication.entity.Comment;
import com.example.myapplication.entity.PersonalInformation;
import com.example.myapplication.entity.Result;
import com.example.myapplication.floatingeditor.EditorCallback;
import com.example.myapplication.floatingeditor.EditorHolder;
import com.example.myapplication.ui.login.LoginActivity;
import com.example.myapplication.utils.TimestampAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ArticleDetailActivity extends AppCompatActivity {
    int ifLike=-1;
    int ifCollect=-1;
    ArticleDetailBinding binding;
    Article article;
    int userId;
    CommentAdapter commentAdapter;
    List<Comment> firstComment;
    List<Comment> secondComment;
    PersonalInformation personalInformation=MainApplication.getInstance().personalInformations;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding=ArticleDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         article = MainApplication.getInstance().articleList.get(getIntent().getIntExtra("index",-1)) ;
        if (article != null) {
            setView(binding,article);

        }
        binding.articleDetailLike.setOnClickListener(view -> {//点赞按钮点击
            if(ifLike>-1){//首先要登录才能点击
                TextView like = view.findViewById(R.id.article_detail_like);
                List<Integer> likeArticleId = personalInformation.likeArticleId;
                if(like.isSelected()){//如果已经选中
                    likeArticleId.remove(Integer.valueOf(article.Id));
                    article.likeNum--;
                }else {
                    likeArticleId.add(Integer.valueOf(article.Id));
                    article.likeNum++;
                }
                like.setSelected(!like.isSelected());
                binding.articleDetailLike.setText(String.valueOf(article.likeNum));
            }else gotoLogin();
        });
        binding.articleDetailCollect.setOnClickListener(view -> {//收藏按钮点击
            if(ifCollect>-1){
                TextView collect = view.findViewById(R.id.article_detail_collect);
                List<Article> attentionArticle = personalInformation.attentionArticle;
                if(collect.isSelected()){//如果已经选中
                    //因为存储的是一个对象，用流操作会更正确
                    attentionArticle=attentionArticle.stream().filter( article1 -> !article1.Id.equals(article.Id)).collect(Collectors.toList());
                    article.collectNumber--;
                }else {
                    attentionArticle.add(article);
                    article.collectNumber++;
                }
                collect.setSelected(!collect.isSelected());
                //将修改后的值传回全局变量
                personalInformation.attentionArticle=attentionArticle;
                binding.articleDetailCollect.setText(String.valueOf(article.collectNumber));
            }else gotoLogin();
        });

        binding.articleDetailCommend.setOnClickListener(view -> {
            if(userId>0){
                FloatEditorActivity.openEditor(ArticleDetailActivity.this,editorCallback1,
                        new EditorHolder(R.layout.fast_reply_floating_layout,
                                R.id.tv_cancel, R.id.tv_submit, R.id.et_content));
            }else gotoLogin();
        });
        Toolbar toolbar=binding.articleDetailAuthorMessage;
        LayoutInflater.from(this).inflate(R.layout.action_article_toolbar_view,toolbar,true);
        // 设置返回按钮的行为
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> finish());

        ImageView imageView = toolbar.findViewById(R.id.article_toolbar_image);//设置作者头像
        Glide.with(toolbar).load(article.authorImage).apply(RequestOptions.circleCropTransform()).into(imageView);
        TextView textView=toolbar.findViewById(R.id.article_toolbar_text);
        textView.setText(article.authorName);//设置作者名字
        toolbar.setClickable(true);
        toolbar.setOnClickListener(view -> {
            new Thread(() -> {
                OkHttpClient okHttpClient = new OkHttpClient();
                Gson gson = new Gson();
                Request build = new Request.Builder().url("http://192.168.1.7:8083/androidUser/getByUsername?username=" + article.authorName).build();
                okHttpClient.newCall(build).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Type resultType = new TypeToken<Result<PersonalInformation>>() {}.getType();
                        Result<PersonalInformation> result = gson.fromJson(response.body().string(), resultType);
                        if(result.getCode()==200){
                            runOnUiThread(() -> {
                                Intent intent = new Intent(ArticleDetailActivity.this, bloggerPage.class);
                                intent.putExtra("blogger",(Serializable) result.getData());
                                startActivity(intent);
                            });
                        }
                    }
                });
            }).start();//先查找博主信息,然后跳转到博主页面


        });
        //获取评论信息
        ListView listView=binding.articleDetailComment;
        //把评论分成一级和二级评论，先过滤排序
         firstComment=article.comments.stream().filter(comment->comment.commentDegree==1).collect(Collectors.toList());
         secondComment=article.comments.stream().filter(comment->comment.commentDegree==2).collect(Collectors.toList());
         commentAdapter = new CommentAdapter(firstComment,secondComment, this);
        listView.setAdapter(commentAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        article.visitNum++;//游览次数+1
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        //查找用户登录，未找到则代表没登录
        userId = sharedPreferences.getInt("userId", -1);
        System.out.println("这次的userId是"+userId);
        article=MainApplication.getInstance().articleList.get(getIntent().getIntExtra("index",-1));
        if(userId>0){//判断该文章是否被点赞和关注,以及更新游览历史

            ifLike=0;ifCollect=0;
            if(personalInformation.likeArticleId!=null){
                for (int i : personalInformation.likeArticleId) {
                    if(article.Id.equals(String.valueOf(i))){
                        binding.articleDetailLike.setSelected(true);
                        ifLike=1;break;
                    }
                }
            }
            if(personalInformation.attentionArticle!=null){
                for (Article article1 : personalInformation.attentionArticle) {
                    if(Objects.equals(article1.Id, article.Id)){
                        binding.articleDetailCollect.setSelected(true);
                        ifCollect=1;break;
                    }
                }
            }

        }
    }

    /**
     * 销毁时调用
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(userId>0){//如果用户已登录,销毁页面时发送后端请求更新文章
            new Thread(() -> {
                OkHttpClient okHttpClient = new OkHttpClient();
                Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampAdapter()).create();
                String json = gson.toJson(article);
                RequestBody create =  RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
                Request build = new Request.Builder().url("http://192.168.1.7:8083/androidArticle/updateArticle").header("Authorization",MainApplication.getInstance().tokenMap.get("tokenHead")+" "+MainApplication.getInstance().tokenMap.get("token")).post(create).build();
                try {
                    Response execute = okHttpClient.newCall(build).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

    }


    EditorCallback editorCallback1 = new EditorCallback() {
        @Override
        public void onCancel() {

        }

        @Override
        public void onSubmit(String content) {//评论提交
            //新建评论
            Comment comment = new Comment();
            comment.content=content;
            PersonalInformation personalInformations = MainApplication.getInstance().personalInformations;
            comment.userImage=personalInformations.userPicture;
            comment.commentUserId= String.valueOf(personalInformations.userId);
            comment.articleId=article.Id;
            comment.username=personalInformations.username;
            comment.createTime=new Timestamp(System.currentTimeMillis());
            comment.parentCommentId="0";
            comment.commentDegree=1;
            article.comments.add(comment);
            firstComment.clear();
            List<Comment> collect = article.comments.stream().filter(comment1 -> comment1.commentDegree == 1).collect(Collectors.toList());
            firstComment.addAll(collect);
            commentAdapter.notifyDataSetChanged();

        }

        @Override
        public void onAttached(ViewGroup rootView) {

        }
    };


    private static void setView(ArticleDetailBinding binding, Article article){
        binding.articleDetailCollect.setText(String.valueOf(article.collectNumber));//设置收藏数
        binding.articleDetailLike.setText(String.valueOf(article.likeNum));//设置点赞数
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(binding.getRoot().getContext(), article.picture);
        binding.articleDetailItemImage.setAdapter(imagePagerAdapter);//设置图片翻页
        binding.articleDetailItemTitle.setText(article.title);//设置标题
        binding.articleDetailItemText.setText(article.content);//设置文本

    }

    public  void gotoLogin(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("您还未登录");
        builder.setMessage("是否选择去登录");
        builder.setPositiveButton("是", (dialogInterface, i) -> {
            Intent intent = new Intent(ArticleDetailActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        builder.setNegativeButton("否", (dialogInterface, i) -> Toast.makeText(ArticleDetailActivity.this,"期待您的登录",Toast.LENGTH_SHORT).show());
        builder.create().show();
    }
}
