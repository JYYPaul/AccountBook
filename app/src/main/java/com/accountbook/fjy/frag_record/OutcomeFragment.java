package com.accountbook.fjy.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.accountbook.fjy.R;

import com.accountbook.fjy.db.DBManager;
import com.accountbook.fjy.db.TypeBean;
import com.accountbook.fjy.utils.KeyBoardUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录页面中的支出模块
 */
public class OutcomeFragment extends Fragment {

    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv,beizhuTv,timeTv;
    GridView typeGv;
    List<TypeBean>typeList;
    private TypeBaseAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        //给GridView填充数据的方法
        loadDataToGV();
        setGVListener();//设置GridView每一项的点击事件
        return view;
    }
    //设置GridView每一项的点击事件
    private void setGVListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetInvalidated(); //提示绘制发生变化
                TypeBean typeBean = typeList.get(position);
                String typename = typeBean.getTypename();
                typeTv.setText(typename);

                int simageId = typeBean.getSimageId();
                typeIv.setImageResource(simageId);
            }
        });
    }

    //给GridView填充数据的方法
    private void loadDataToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);
        //获取数据库中的数据源
        List<TypeBean> outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        //让自定义键盘显示
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        boardUtils.showKeyboard();
        //设置接口，监听确定按钮被点击
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //点击确认按钮
                //获取记录的信息，保存在数据库中
                //返回上级页面

            }
        });
    }
}