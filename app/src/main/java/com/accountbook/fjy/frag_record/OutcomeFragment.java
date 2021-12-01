package com.accountbook.fjy.frag_record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.accountbook.fjy.R;
import com.accountbook.fjy.db.DBManager;
import com.accountbook.fjy.db.TypeBean;

import java.util.List;


public class OutcomeFragment extends BaseRecordFragment {


    //重写
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //获取数据库中的数据源
        List<TypeBean> outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);

    }

    @Override
    public void saveAccountToDB() {

    }
}