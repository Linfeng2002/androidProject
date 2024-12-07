package com.example.myapplication.Fragment.my;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.MainApplication;
import com.example.myapplication.R;
import com.example.myapplication.adapter.ArticleAdapter;
import com.example.myapplication.bloggerPage;
import com.example.myapplication.databinding.FragmentMyBinding;
import com.example.myapplication.entity.PersonalInformation;
import com.example.myapplication.entity.Article;
import com.example.myapplication.history;
import com.example.myapplication.myOrder;
import com.example.myapplication.ui.login.LoginActivity;

import java.io.Serializable;

public class MyFragment extends Fragment {

    private FragmentMyBinding binding;
    PersonalInformation personalInformation;

    int userId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.myMessagePicture.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), bloggerPage.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("blogger", personalInformation);
            intent.putExtra("blogger",bundle);
            startActivity(intent);
        });

        //设置攻略推荐
        ArticleAdapter articleAdapter = new ArticleAdapter(getContext(), Article.getDefault());
        binding.myMessageRecommend.setAdapter(articleAdapter);
        binding.myMessageRecommend.setOnItemClickListener((adapterView, view, i, l) -> {

        });//推荐
        binding.myMessageRecordArticle.setOnClickListener(view -> {
            if(userId<0){
                gotoLogin();
            }else{
                Intent intent = new Intent(getActivity(), history.class);
                intent.putExtra("status","游览记录");
                startActivity(intent);
            }


        });//游览记录
        binding.myMessageFavorArticle.setOnClickListener(view -> {
            if(userId<0){
                gotoLogin();
            }else {
                Intent intent = new Intent(getActivity(), history.class);
                intent.putExtra("status","收藏记录");
                startActivity(intent);
            }

        });//收藏记录
        binding.myMessageDiscountCoupon.setOnClickListener(view -> {
            if(userId<0){
                gotoLogin();
            }
        });//优惠券
        binding.myMessageAllOrder.setOnClickListener(view -> {
            if(userId<0){
                gotoLogin();
            }else gotoMyOrder("全部订单");
        });//全部订单
        binding.myMessageUnpay.setOnClickListener(view->{
            if(userId<0){
                gotoLogin();
            }else gotoMyOrder("待支付");
        });//待支付
        binding.myMessageUnjourney.setOnClickListener(view->{
            if(userId<0){
                gotoLogin();
            }else gotoMyOrder("待出行");
        });//待出行
        binding.myMessageUncommend.setOnClickListener(view->{
            if(userId<0){
                gotoLogin();
            }else gotoMyOrder("待评价");
        });//待评价
        binding.myMessageLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("尊敬的用户"); // 设置对话框的标题文本
                builder.setMessage("是否确定登出？"); // 设置对话框的内容文本
                builder.setPositiveButton("登出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            logout();
                        }
                    });
                builder.setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                AlertDialog alert = builder.create(); // 根据建造器构建提醒对话框对象
                alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        personalInformation= MainApplication.getInstance().personalInformations;
                        //设置收藏，游览历史，优惠券数量
                        binding.myMessageFavorArticle.setText(getString(R.string.likeHistory,personalInformation.attentionArticle.size()));
                        binding.myMessageRecordArticle.setText(getString(R.string.recordHistory,personalInformation.recordArticle.size()));
                        //配置名字和头像
                        binding.myMessageName.setText(personalInformation.username);
                        Glide.with(getActivity()).load(personalInformation.userPicture).apply(RequestOptions.circleCropTransform()).into(binding.myMessagePicture);
                        //设置点赞数关注数粉丝数
                        binding.myMessageFollowerNumber.setText(getString(R.string.follower_number,personalInformation.follower.size()));
                        binding.myMessageFanNumber.setText(getString(R.string.fans_number,personalInformation.fans.size()));
                        binding.myMessageLikeNumber.setText("点赞"+String.valueOf(personalInformation.likeNumber));
                    }
                });
                alert.show(); // 显示提醒对话框
            }
        });
        return root;
    }

    /**
     * 数据的设置放在onresume中
     */
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
        //从全局变量中获取用户信息
        personalInformation= MainApplication.getInstance().personalInformations;
        //设置收藏，游览历史，优惠券数量
        binding.myMessageFavorArticle.setText(getString(R.string.likeHistory,personalInformation.attentionArticle.size()));
        binding.myMessageRecordArticle.setText(getString(R.string.recordHistory,personalInformation.recordArticle.size()));
        //配置名字和头像
        binding.myMessageName.setText(personalInformation.username);
        Glide.with(getActivity()).load(personalInformation.userPicture).apply(RequestOptions.circleCropTransform()).into(binding.myMessagePicture);
        //设置点赞数关注数粉丝数
        binding.myMessageFollowerNumber.setText(getString(R.string.follower_number,personalInformation.follower.size()));
        binding.myMessageFanNumber.setText(getString(R.string.fans_number,personalInformation.fans.size()));
        binding.myMessageLikeNumber.setText("点赞"+String.valueOf(personalInformation.likeNumber));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * 登出
     */
    public void logout(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);  // 设置为已登录
        editor.putInt("userId", 0);//设置登录的id
        editor.apply();
        MainApplication.getInstance().tokenMap.clear();
        MainApplication.getInstance().personalInformations=PersonalInformation.getDefaultPersonalInformation();
        Toast.makeText(getActivity(),"登出成功",Toast.LENGTH_SHORT).show();
    }

    public void gotoMyOrder(String state){
        Intent intent = new Intent(getActivity(), myOrder.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("myOrder", (Serializable) personalInformation.userOrder);
        bundle.putString("state",state);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public  void gotoLogin(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("您还未登录");
        builder.setMessage("是否选择去登录");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(),"期待您的登录",Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }
}