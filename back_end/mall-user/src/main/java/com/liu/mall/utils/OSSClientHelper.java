package com.liu.mall.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyuncs.exceptions.ClientException;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class OSSClientHelper {
    @Value("${aliyun.oss.bucketName}")
    private  String ALIYUN_OSS_BUCKET_NAME;
    @Value("${aliyun.oss.endpoint}")
    private  String ALIYUN_OSS_ENDPOINT;
    @Value("${aliyun.oss.accessKeyId}")
    private  String ALIYUN_OSS_KEYID;
    @Value("${aliyun.oss.accessKeySecret}")
    private  String ALIYUN_OSS_KEYSERCET;


    private static OSS clint;
    private static OSS ossClint;
    private static EnvironmentVariableCredentialsProvider credentialsProvider;


    /**
     * 上传文件到oss
     * @param inputStream 文件输入流
     * @return 文件存储路径
     */

    public  String upload(InputStream inputStream, Integer id)  {
        clint= new OSSClientBuilder().build(ALIYUN_OSS_ENDPOINT, ALIYUN_OSS_KEYID, ALIYUN_OSS_KEYSERCET);
        //以时间戳以及用户id为文件名，防止冲突
        String fileName="Image/"+System.currentTimeMillis()+id+".jpg";
        clint.putObject(ALIYUN_OSS_BUCKET_NAME,fileName,inputStream);
        return ALIYUN_OSS_ENDPOINT.split("//")[0] + "//" + ALIYUN_OSS_BUCKET_NAME + "." + ALIYUN_OSS_ENDPOINT.split("//")[1] + "/" + fileName;

    }


}
