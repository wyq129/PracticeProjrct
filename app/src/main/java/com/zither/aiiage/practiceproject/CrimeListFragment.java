package com.zither.aiiage.practiceproject;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zither.aiiage.practiceproject.sqlLite.CrimeBean;
import com.zither.aiiage.practiceproject.sqlLite.DatebaseHelper;

import java.util.List;


/**
 *
 * @author wangyanqin
 * @date 2018/08/15
 * SwipeRefreshLayout:SwipeRefreshLayout
 */
public class CrimeListFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private TextView mTextView;
    private static final int REQUEST_CRIME=1;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //是否刷新中
    private boolean isRefresh = false;
    DatebaseHelper datebaseHelper;
    List<CrimeBean> list;
    public CrimeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_crime_list, container, false);
        initView(view);
        updateUI();
        return view;
    }

    private void initView(View view) {
        mRecyclerView=view.findViewById(R.id.crimelist_recycleView);
        mTextView=view.findViewById(R.id.tv_crimeList_add);
        mSwipeRefreshLayout=view.findViewById(R.id.crimeList_swipeLayout);
        //设置进度条的颜色主题，最多能设置四种，加载颜色是循环播放的，只要没有完成刷新就会一直循环.
        mSwipeRefreshLayout.setColorSchemeColors(Color.GREEN);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //设置手指在下拉多少距离会触发下拉刷新
        mSwipeRefreshLayout.setDistanceToTriggerSync(300);
        //设置下拉圆圈的背景
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        //设置圆圈的大小
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        // 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivityForResult(intent,REQUEST_CRIME);
            }
        });
    }


    private void updateUI() {
       datebaseHelper=new DatebaseHelper(getActivity());
       list=datebaseHelper.getAllCrimeBean();
        if (mCrimeAdapter==null) {
            mCrimeAdapter = new CrimeAdapter(list,getActivity());
            mRecyclerView.setAdapter(mCrimeAdapter);
            mCrimeAdapter.notifyDataSetChanged();
        }
        else {
            //刷新的另一种方式
           /* mCrimeAdapter.setCrime(list);
            mCrimeAdapter.notifyDataSetChanged();*/
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

    @Override
    public void onRefresh() {
        if (!isRefresh){
            isRefresh=true;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                    DatebaseHelper datebaseHelper=new DatebaseHelper(getActivity());
                    List<CrimeBean> list=datebaseHelper.getAllCrimeBean();
                    //修改adapter的数据
                    mCrimeAdapter.setCrime(list);
                    mCrimeAdapter.notifyDataSetChanged();
                    isRefresh = false;
                }
            },1000);
        }
    }
}
