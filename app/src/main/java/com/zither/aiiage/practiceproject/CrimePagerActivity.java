package com.zither.aiiage.practiceproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zither.aiiage.practiceproject.sqlLite.CrimeBean;
import com.zither.aiiage.practiceproject.sqlLite.DatebaseHelper;

import java.util.List;

/**
 * 实现左右滑动
 * @author wangyanqin
 * @date 2018/08/17
 */
public class CrimePagerActivity extends AppCompatActivity {
    List<CrimeBean> mCrimeBean;
    ViewPager mViewPager;
    private static final String CRIME_ID = "crime_id";

    public static Intent newInstance(Context context, int id) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(CRIME_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        mViewPager=findViewById(R.id.activity_crime_pager);
        DatebaseHelper datebaseHelper=new DatebaseHelper(CrimePagerActivity.this);
        mCrimeBean=datebaseHelper.getAllCrimeBean();
        int crimeId = getIntent().getIntExtra(CRIME_ID,-1);
        FragmentManager manager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(manager) {
            @Override
            public Fragment getItem(int i) {
                CrimeBean crimeBean=mCrimeBean.get(i);
                return CrimePagerFragment.newInstance(crimeBean.getId());
            }

            @Override
            public int getCount() {
                return mCrimeBean.size();
            }
        });
        for (int i = 0; i < mCrimeBean.size(); i++) {
            if (mCrimeBean.get(i).getId()==crimeId) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
