package com.zither.aiiage.practiceproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zither.aiiage.practiceproject.sqlLite.CrimeBean;
import com.zither.aiiage.practiceproject.sqlLite.DatebaseHelper;

import java.util.List;

/**
 * @author wangyanqin
 * @date 2018/08/21
 */
public class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
    private List<CrimeBean> mBeanList;
    Context mContext;

    public CrimeAdapter(List<CrimeBean> crimeBeans, Context context) {
        mBeanList = crimeBeans;
        mContext = context;
    }

    @NonNull
    @Override
    public CrimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            return new CrimeHolder(layoutInflater, viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull final CrimeHolder viewHolder, int i) {
            final CrimeBean crimeBean = mBeanList.get(i);
            viewHolder.bind(crimeBean);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = CrimePagerActivity.newInstance(mContext, crimeBean.getId());
                    mContext.startActivity(intent);
                }
            });
            //长按删除
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(mContext).setTitle("确定删除吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatebaseHelper datebaseHelper = new DatebaseHelper(mContext);
                            mBeanList.remove(crimeBean);
                            datebaseHelper.deleteCrimeById(crimeBean.getId());
                            notifyItemRemoved(i);
                            notifyDataSetChanged();
                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("取消", null).create().show();
                    return false;
                }
            });
    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    public void setCrime(List<CrimeBean> list) {
        mBeanList = list;
    }
}

