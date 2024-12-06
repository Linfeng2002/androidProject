package com.example.myapplication;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.utils.ImageUtil;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> pickImagesLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //得到xml文件主体
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //配置创建文章的点击事件
        binding.fab.setOnClickListener(view -> {

        });
        //将activity和menu和fragment关联起来
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_message, R.id.navigation_journey,R.id.navigation_my)
                .build();
        NavHostFragment navHostFragment =(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);

        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION}, 1);
//        }

        getSupportActionBar().hide();
        // 初始化 ActivityResultLauncher
        pickImagesLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                ArrayList<Uri> uris = new ArrayList<>();
                                if (data.getClipData() != null) {
                                    int count = data.getClipData().getItemCount();
                                    for (int i = 0; i < count; i++) {
                                        uris.add(data.getClipData().getItemAt(i).getUri());
                                    }
                                } else if (data.getData() != null) {
                                    uris.add(data.getData());
                                }
                                // 处理选中的图片URI
                                Intent intent = new Intent(MainActivity.this, newArticle.class);
                                intent.putParcelableArrayListExtra("stringList", uris);
                                startActivity(intent);
                            }
                        }
                    }
                });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImages();
            }
        });
    }
    private void pickImages() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        pickImagesLauncher.launch(intent);
    }





}