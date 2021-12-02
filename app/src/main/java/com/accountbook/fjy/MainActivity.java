package com.accountbook.fjy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.accountbook.fjy.adapter.AccountAdapter;
import com.accountbook.fjy.db.AccountBean;
import com.accountbook.fjy.db.DBManager;
import com.accountbook.fjy.utils.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView todayLv;   //展示今天收支情况的ListView
    ImageView searchIv;
    Button editBtn;
    ImageButton moreBtn;
    //声明数据源
    List<AccountBean>mDatas;
    AccountAdapter adapter;
    int year,month,day;
    //头布局相关控件
    View headerView;
    TextView topOutTv,topInTv,topTotalTv,topConTv;
    ImageView topShowIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        //添加ListView头布局
        addLVHeaderView();
        mDatas = new ArrayList<>();
        //设置适配器：加载每行数据到列表中
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);

    }

    //初始化自带的View的方法
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_more);
        //searchIv = findViewById(R.id.main_iv_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        //searchIv.setOnClickListener(this);
        setLVLongClickListener();
    }

    //设置ListView的长按事件
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {    //点击头布局
                    return false;
                }
                int pos = position - 1;
                AccountBean clickBean = mDatas.get(pos);    //获取正在被点击的信息
                //弹出提示是否删除的对话框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }

    //弹出是否删除某条记录的对话框
    private void showDeleteItemDialog(final AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("您确定要删除这条记录吗？")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        //执行删除操作
                        DBManager.deleteItemFromAccounttbById(click_id);
                        mDatas.remove(clickBean);   //实时刷新，移除集合中的对象
                        adapter.notifyDataSetChanged(); //提示适配器更新数据
                        setTopTvShow(); //改变头布局TextView显示的内容
                    }
                });
        builder.create().show();    //显示对话框
    }

    //给ListView添加头布局的方法
    private void addLVHeaderView() {
        //将布局转换为View对象
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);
        //查找头布局可用控件
        topTotalTv = headerView.findViewById(R.id.item_mainlv_top_tv_total);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);

    }

    //获取今日具体时间
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
        setTopTvShow();
    }

    //设置头布局中文本内容显示
    private void setTopTvShow() {
        //获取今日支出和收入总金额并显示在View中
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "今日支出 ￥" + outcomeOneDay + " 收入 ￥" + incomeOneDay;
        topConTv.setText(infoOneDay);
        //获取本月支出和收入总金额
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        topInTv.setText("￥"+incomeOneMonth);
        topOutTv.setText("￥"+outcomeOneMonth);
        //获取净资产
        float totalOneYear = DBManager.getSumMoneyOneYear(year,1)-DBManager.getSumMoneyOneYear(year,0);
        topTotalTv.setText("￥"+totalOneYear);
    }

    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }


    //
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_edit:
                Intent it1 = new Intent(this, RecordActivity.class);    //跳转界面
                startActivity(it1);
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;
            case R.id.item_mainlv_top_iv_hide:
                //切换TextView明文和密文
                toggleShow();
                break;

        }
        if (v == headerView) {
            //头布局被点击
        }
    }

    boolean isShow = true;
    //点击眼睛时，切换明文和密文
    private void toggleShow() {
        if (isShow) {   //明文-->密文
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(passwordMethod);  //设置隐藏
            topOutTv.setTransformationMethod(passwordMethod);  //设置隐藏
            topTotalTv.setTransformationMethod(passwordMethod);  //设置隐藏
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShow = false; //设置标志位为隐藏状态
        }
        else {          //密文-->明文
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(hideMethod);  //设置隐藏
            topOutTv.setTransformationMethod(hideMethod);  //设置隐藏
            topTotalTv.setTransformationMethod(hideMethod);  //设置隐藏
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow = true; //设置标志位为显示状态
        }
    }
}