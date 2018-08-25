package com.zither.aiiage.practiceproject;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zither.aiiage.practiceproject.sqlLite.CrimeBean;
import com.zither.aiiage.practiceproject.sqlLite.DatebaseHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * @author wangyanqin
 * @date 2018/08/15
 * SwipeRefreshLayout:SwipeRefreshLayout
 */
public class CrimeListFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnTouchListener {
    private static final String TAG = "CrimeListFragment";
    private RecyclerView mRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private TextView mTextView;
    private static final int REQUEST_CRIME = 1;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mLinearLayout;
    //是否刷新中
    private boolean isRefresh = false;
    DatebaseHelper datebaseHelper;
    List<CrimeBean> list;
    LinearLayoutManager layoutManager;
    int loadTimes = 0;
    private View loadMoreIv;
    private boolean loadMoreCrime = false;
    List<CrimeBean> crimeBeanList = new ArrayList<>();
    public CrimeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        datebaseHelper = new DatebaseHelper(getActivity());
        list = datebaseHelper.getAllCrimeBean();
        initView(view);
        loadMoreData();
        updateUI();
        return view;
    }

    RecyclerView.OnScrollListener scrollChangeListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && !recyclerView.canScrollVertically(1)) {
                if (loadMoreCrime) {
                    return;
                }
                mLinearLayout.setVisibility(View.VISIBLE);
                loadMoreIv.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate));
                loadMoreCrime = true;
                recyclerView.scrollToPosition(crimeBeanList.size() - 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreData();
                        mCrimeAdapter.notifyDataSetChanged();
                        loadMoreCrime = false;
                        mLinearLayout.setVisibility(View.INVISIBLE);
                        loadMoreIv.clearAnimation();
                    }
                }, 1000);
                mSwipeRefreshLayout.setEnabled(true);
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    };

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.crimelist_recycleView);
        mTextView = view.findViewById(R.id.tv_crimeList_add);
        mSwipeRefreshLayout = view.findViewById(R.id.crimeList_swipeLayout);
        mLinearLayout = view.findViewById(R.id.crime_list_tv_load_more);
        loadMoreIv = view.findViewById(R.id.crime_list_iv_loading_bottom);
        //设置进度条的颜色主题，最多能设置四种，加载颜色是循环播放的，只要没有完成刷新就会一直循环.
        mSwipeRefreshLayout.setColorSchemeColors(Color.GREEN);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //设置手指在下拉多少距离会触发下拉刷新
        mSwipeRefreshLayout.setDistanceToTriggerSync(300);
        //设置下拉圆圈的背景
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        //设置圆圈的大小
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        // 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnTouchListener(this);
        //初始化手势监听
        initGestureDetector();
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivityForResult(intent, REQUEST_CRIME);
            }
        });
    }

    /**
     * 加载更多数据，每次最多加载8条
     */
    private void loadMoreData() {
        loadTimes++;
        int preLoadCount = 8;
        if (crimeBeanList != null) {
            crimeBeanList.clear();
        }
        for (int i = 0; i < loadTimes * preLoadCount; i++) {
            if (i >= list.size()) {
                Toast.makeText(getActivity(), "数据加载完了", Toast.LENGTH_SHORT).show();
                break;
            } else {
                crimeBeanList.add(list.get(i));
                Log.d(TAG, "loadMoreData: " + crimeBeanList);
            }

        }

    }

    //更新UI
    private void updateUI() {
        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimeBeanList, getActivity());
            mRecyclerView.setAdapter(mCrimeAdapter);
            mCrimeAdapter.notifyDataSetChanged();
        } else {
            //刷新的另一种方式
            mCrimeAdapter.setCrime(crimeBeanList);
            mCrimeAdapter.notifyDataSetChanged();
        }
    }

    //添加返回bean
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CRIME) {
            if (resultCode == 2) {
                if (data != null) {
                    updateUI();
                }
            }
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
            mRecyclerView.setEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                    //修改adapter的数据
                    mCrimeAdapter.setCrime(list);
                    mCrimeAdapter.notifyDataSetChanged();
                    isRefresh = false;
                }
            }, 1000);
            mRecyclerView.setEnabled(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loadMoreIv != null) {
            loadMoreIv.clearAnimation();
        }
    }

    GestureDetector mDetector;
    float startPointEvent, stopPointEvent;

    /**
     * 触摸事件
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            startPointEvent = motionEvent.getY();
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            stopPointEvent = motionEvent.getY();
        }
        return mDetector.onTouchEvent(motionEvent);
    }

    /**
     * 手势监听及判断是下拉刷新还是上拉加载数据
     */
    private void initGestureDetector() {
        mDetector = new GestureDetector(getActivity(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                float y = stopPointEvent - startPointEvent;
                Log.d(TAG, "onScroll: " + y);
                if (y > 0) {
                    mRecyclerView.addOnScrollListener(scrollChangeListener);
                    mSwipeRefreshLayout.setEnabled(false);
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                float y = motionEvent1.getY() - startPointEvent;
                Log.d(TAG, "onScroll: " + y);
                if (y < 0) {
                    mRecyclerView.addOnScrollListener(scrollChangeListener);
                    mSwipeRefreshLayout.setEnabled(false);
                }
                return false;
            }
        });
    }
}
