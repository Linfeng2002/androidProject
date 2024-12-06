package com.example.myapplication.floatingeditor;



import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

import java.io.Serializable;

/**
 * 创建日期：2017/9/14.
 *
 * @author kevin
 */

public class EditorHolder implements Serializable{
    public int layoutResId;
    public int cancelViewId;
    public int submitViewId;
    public int editTextId;
    public EditorHolder(@LayoutRes int layoutResId, @IdRes int cancelViewId,
                        @IdRes int submitViewId, @IdRes int editTextId){
        this.layoutResId = layoutResId;
        this.cancelViewId = cancelViewId;
        this.submitViewId = submitViewId;
        this.editTextId = editTextId;
    }
}
