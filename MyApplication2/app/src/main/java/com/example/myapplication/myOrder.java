package com.example.myapplication;

import android.os.Bundle;
import com.example.myapplication.adapter.OrderAdapter;
import com.example.myapplication.adapter.OrderStateAdapter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import com.example.myapplication.databinding.ActivityMyOrderBinding;
import com.example.myapplication.entity.Order;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class myOrder extends AppCompatActivity {

    private ActivityMyOrderBinding binding;
    List<Order> orderList=new ArrayList<>();
    List<Order> myOrder=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取用户信息
        Bundle bundle = getIntent().getExtras();
        String state=bundle.getString("state");
        myOrder = (ArrayList<Order>) bundle.getSerializable("myOrder");
        myOrder=myOrder.stream().sorted(Comparator.comparingInt(Order::getState)).collect(Collectors.toList());
        binding = ActivityMyOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (getSupportActionBar() != null) { getSupportActionBar().setDisplayHomeAsUpEnabled(true); getSupportActionBar().setDisplayShowHomeEnabled(true); }
        ArrayList<String> objects = new ArrayList<>();
        objects.add("全部订单");objects.add("待付款");objects.add("待出行");objects.add("待评价");
        OrderStateAdapter orderStateAdapter = new OrderStateAdapter(objects,this);
        ListView listView =binding.orderState;
        listView.setAdapter(orderStateAdapter);//状态信息选择

        ListView orderDetail=binding.orderDetail;//具体订单信息
        OrderAdapter orderAdapter = new OrderAdapter(this, orderList);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            orderStateAdapter.setSelectedPosition(i);//通知状态变化
            orderList.clear();
            orderList= myOrder.stream().filter(order->order.State!=i).collect(Collectors.toList());//过滤状态不为i的订单
            orderAdapter.notifyDataSetChanged();//通知适配器进行更改
        });//状态切换的点击效果

        switch (state){//判断初始状态
            case "全部订单":    createOrder(0);break;
            case "待付款" :    createOrder(1);break;
            case "待出行" :    createOrder(2);break;
            case "待评价" :    createOrder(3);break;
        }
        orderDetail.setOnItemClickListener((adapterView, view, i, l) -> {

        });//点击订单跳转
        setSupportActionBar(binding.orderSearch);

        if (getSupportActionBar() != null) { getSupportActionBar().setDisplayHomeAsUpEnabled(true); getSupportActionBar().setDisplayShowHomeEnabled(true); }
        binding.orderSearch.setNavigationOnClickListener(view -> finish());
        addMenuProvider(new MenuProvider() {//添加菜单配置
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_search,menu);
                MenuItem item = menu.findItem(R.id.action_search);
                SearchView actionView = (SearchView) item.getActionView();
                actionView.setBackgroundResource(R.drawable.shape_edit_normal);
                actionView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                actionView.setQueryHint("搜索你的订单");
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) actionView.getLayoutParams();
                params.setMargins(0, 10, 0, 10); // 左、上、右、下的外边距，单位是像素（px）
                actionView.setLayoutParams(params);
                actionView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        //处理搜索提交
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        //实时搜索处理
                        return false;
                    }
                });

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId()==android.R.id.home){
                    finish();
                    return true;
                }
                return myOrder.super.onOptionsItemSelected(menuItem);
            }
        },this);
        // 设置导航按钮点击事件
        binding.orderSearch.setNavigationOnClickListener(view -> finish());
    }

    /**
     * 配置跳转到此活动页面时的初始状态
     * @param num
     */
    public void createOrder(int num){
        binding.orderState.setSelection(num);
        binding.orderState.performItemClick(binding.orderState.getChildAt(num),num,binding.orderState.getItemIdAtPosition(num));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return true;
    }

}