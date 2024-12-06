package com.example.myapplication.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    // 把字符串保存到指定路径的文本文件
    public static void saveText(String path, String txt) {
        BufferedWriter os = null;
        try {
            os = new BufferedWriter(new FileWriter(path));
            os.write(txt);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 从指定路径的文本文件中读取内容字符串
    public static String openText(String path) {
        BufferedReader is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = new BufferedReader(new FileReader(path));
            String line = null;
            while ((line = is.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 把位图数据保存到指定路径的图片文件
     * @param path
     * @param bitmap 位图集合
     */
    public static void saveImage(String path, List<Bitmap> bitmap) {
        FileOutputStream fos = null;
        try {
            for (Bitmap bitmap1 : bitmap) {
                fos = new FileOutputStream(path+System.currentTimeMillis()+".jpeg");
                // 把位图数据压缩到文件输出流中
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从指定路径的图片文件中读取位图数据
     * @param path 图片路径集合
     * @return
     */
    public static List<Bitmap> openImage(List<String> path) {

        List<Bitmap> list=new ArrayList<>();
        for (String s : path) {
            Bitmap bitmap = null;
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(s);
                bitmap = BitmapFactory.decodeStream(fis);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            list.add(bitmap);
        }

        return list;
    }
}
