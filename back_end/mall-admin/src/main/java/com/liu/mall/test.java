package com.liu.mall;

import org.junit.Test;

public class test {
    @Test
    public void candy(){
        int[] height=new int[]{4,2,0,3,2,5};
        int length=height.length;
        int k=-1;
        //if(height[0]>0) k=0;
        int sum=0,cout=0;
        for(int i=0;i<length;i++){
            if(k>=0){
                if(height[i]<height[k]) cout+=height[k]-height[i];
                else{
                    sum+=cout;
                    k=i;
                }
            }else{
                if(height[i]>0) k=i;
            }
        }
        System.out.println(sum);
    }
}
