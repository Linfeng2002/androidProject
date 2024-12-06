package com.example.myapplication.floatingeditor;



import androidx.annotation.StringRes;

import java.io.Serializable;

/**
 * Created by like on 2017/9/18.
 */

public class InputCheckRule implements Serializable {
    public int minLength;  //  最少输入字符数
    public int maxLength;  // 最多输入字符数
    public String regxRule;    // 正则表达式校验
    public int regxWarn;   // 正则表达式失败提示

    public InputCheckRule(int maxLength, int minLength) {
        this(maxLength,minLength, null, 0);
    }

    public InputCheckRule(int maxLength, int minLength, String regxRule, @StringRes int regxWarn) {
        if (maxLength < minLength || minLength < 0)
            throw new IllegalStateException("maxLength < minLength or minLength < 0");
        this.maxLength = maxLength;
        this.minLength = minLength;
        this.regxRule = regxRule;
        this.regxWarn = regxWarn;
    }
}
