package com.accountbook.fjy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.accountbook.fjy.adapter.AccountAdapter;
import com.accountbook.fjy.db.AccountBean;
import com.accountbook.fjy.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView todayLv;   //展示今天收支情况的ListView
    //声明数据源
    List<AccountBean>mDatas;
    AccountAdapter adapter;
    int year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        todayLv = findViewById(R.id.main_lv);
        mDatas = new ArrayList<>();
        //设置适配器：加载每行数据到列表中
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    //当Activity获取焦点时调用的方法
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
    }

    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_iv_search:

                break;
            case R.id.main_btn_edit:
                Intent it1 = new Intent(this, RecordActivity.class);    //跳转界面
                startActivity(it1);
                break;
            case R.id.main_btn_more:

                break;
        }
    }
}