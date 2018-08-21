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
            mCrimeAdapter = new CrimeAdapter(list);
            mRecyclerView.setAdapter(mCrimeAdapter);
            mCrimeAdapter.notifyDataSetChanged();
        }
        else {
            mCrimeAdapter.setCrime(list);
            mCrimeAdapter.notifyDataSetChanged();
        }
    }

    public class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<CrimeBean> mBeanList;

        public CrimeAdapter(List<CrimeBean> crimeBeans) {
            mBeanList=crimeBeans;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater,viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder crimeHolder, int i) {
            final CrimeBean crimeBean=mBeanList.get(i);
            crimeHolder.bind(crimeBean);
            crimeHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(getActivity()).setTitle("确定删除吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getActivity(), "长按", Toast.LENGTH_SHORT).show();
                            DatebaseHelper datebaseHelper=new DatebaseHelper(getActivity());
                              mBeanList.remove(crimeBean);
                            datebaseHelper.deleteCrimeById(crimeBean.getId());
                              mCrimeAdapter.notifyItemRemoved(i);
                              notifyDataSetChanged();
                        }
                    }).setNegativeButton("取消",null).create().show();
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mBeanList.size();
        }

        public void setCrime(List<CrimeBean> list) {
            mBeanList=list;
        }
    }
    public TextView mTitle,mDate,solved;
    public class CrimeHolder extends RecyclerView.ViewHolder{

        private CrimeBean mCrimeBean;
        private LinearLayout mLinearLayout;
        public void  bind(CrimeBean crimeBean){
            mCrimeBean= crimeBean;
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=CrimePagerActivity.newInstance(getActivity(),mCrimeBean.getId());
                    startActivity(intent);
                }
            });

        }
        public CrimeHolder(LayoutInflater linearLayout,ViewGroup viewGroup){
            super(linearLayout.inflate(R.layout.crimelist_item,viewGroup,false));
            mTitle=itemView.findViewById(R.id.crimelist_title);
            mDate=itemView.findViewById(R.id.crimelist_date);
            solved=itemView.findViewById(R.id.tv_solved);
            mLinearLayout=itemView.findViewById(R.id.iv_unsolved);
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
