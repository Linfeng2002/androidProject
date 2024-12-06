package com.example.myapplication.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageUtil {

    private static ExecutorService executorService = Executors.newFixedThreadPool(4);

    /**
     * 并发下载
     * @param context
     * @param urls
     */
    public static List<String> downloadAndSaveImage(Context context, List<String> urls){
        //使用集合存储返回的本地路径结果
        List<String> result=new ArrayList<>();
        for (String url : urls) {
            String path=context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString()+"/"+System.currentTimeMillis()+".jpeg";
            result.add(path);
            executorService.execute(() -> downloadAndSaveImage(context,url,path));
        }
        return result;
    }
    public static String downloadAndSaveImage(Context context, String url,String path) {
        OkHttpClient client = new OkHttpClient();
//        final File[] file = new File[1];
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败处理
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // 将图片保存到本地
                try (InputStream inputStream = response.body().byteStream();
                     FileOutputStream outputStream = new FileOutputStream(path)) {
                    byte[] buffer = new byte[2048];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return path;
    }


    public static String getAppDownloadDirectory(Context context) {
        // 获取外部存储中的下载目录
        File downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (downloadDir != null && !downloadDir.exists()) {
            downloadDir.mkdirs(); // 如果目录不存在，则创建
        }
        return downloadDir.toString()+"/";
    }

}
