package com.zither.aiiage.practiceproject;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zither.aiiage.practiceproject.sqlLite.CrimeBean;

/**
 * @author wangyanqin
 * @date 2018/08/21
 */
public class CrimeHolder extends RecyclerView.ViewHolder{
    public TextView mTitle,mDate,solved;
    private CrimeBean mCrimeBean;
    private LinearLayout mLinearLayout;
    public void  bind(CrimeBean crimeBean){
        mCrimeBean= crimeBean;
        if (crimeBean==null){
            return;
        }
        mTitle.setText(crimeBean.getName());
        mDate.setText(crimeBean.getDate().toString());
        System.out.println("mCrimeBean"+mCrimeBean.toString());
        if (mCrimeBean.isSolved()==false){
            mLinearLayout.setVisibility(View.VISIBLE);
            solved.setVisibility(View.GONE);
        }else {
            solved.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.GONE);
        }
    }
    public CrimeHolder(LayoutInflater linearLayout, ViewGroup viewGroup){
        super(linearLayout.inflate(R.layout.crimelist_item,viewGroup,false));
        mTitle=itemView.findViewById(R.id.crimelist_title);
        mDate=itemView.findViewById(R.id.crimelist_date);
        solved=itemView.findViewById(R.id.tv_solved);
        mLinearLayout=itemView.findViewById(R.id.iv_unsolved);
    }
}
