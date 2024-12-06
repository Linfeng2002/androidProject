package com.liu.mall.service;

import com.liu.mall.model.OssCallbackResult;
import com.liu.mall.model.OssPolicyResult;
import javax.servlet.http.HttpServletRequest;

/**
 * Oss对象存储管理Service
 * Created by macro on 2023/5/17.
 */
public interface OssService {
    /**
     * Oss上传策略生成
     */
    OssPolicyResult policy();
    /**
     * Oss上传成功回调
     */
    OssCallbackResult callback(HttpServletRequest request);
}
