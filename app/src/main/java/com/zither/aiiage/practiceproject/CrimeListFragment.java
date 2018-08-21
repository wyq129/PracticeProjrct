package com.zither.aiiage.practiceproject;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zither.aiiage.practiceproject.sqlLite.CrimeBean;
import com.zither.aiiage.practiceproject.sqlLite.DatebaseHelper;

import java.util.List;


/**
 *
 * @author wangyanqin
 * @date 2018/08/15
 */
public class CrimeListFragment extends android.support.v4.app.Fragment {
    private RecyclerView mRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private TextView mTextView;
    private static final int REQUEST_CRIME=1;
    public CrimeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_crime_list, container, false);
        mRecyclerView=view.findViewById(R.id.crimelist_recycleView);
        mTextView=view.findViewById(R.id.tv_crimeList_add);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivityForResult(intent,REQUEST_CRIME);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        updateUI();
        return view;
    }

    private void updateUI() {
        DatebaseHelper datebaseHelper=new DatebaseHelper(getActivity());
        List<CrimeBean> list=datebaseHelper.getAllCrimeBean();
        if (mCrimeAdapter==null) {
            mCrimeAdapter = new CrimeAdapter(list,getActivity());
            mRecyclerView.setAdapter(mCrimeAdapter);
            mCrimeAdapter.notifyDataSetChanged();
        }
        else {
            mCrimeAdapter.setCrime(list);
            mCrimeAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CRIME){
            if (resultCode==2){
                if (data!=null){
                  updateUI();
                }
            }
        }
    }
}
