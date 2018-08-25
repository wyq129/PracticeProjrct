package com.zither.aiiage.practiceproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bluemobi.dylan.photoview.library.PhotoViewAttacher;

/**
 * @author wangyanqin
 * @date 2018/08/
 */
public class ShowPhotoActivity extends AppCompatActivity {
    private static final String BITMAP="bitmap";
    @BindView(R.id.iv_showPhoto_Picture)
    ImageView ivShowPhotoPicture;
    Bitmap mBitmap;
    String path;
    private PhotoViewAttacher mPhotoViewAttacher;
    public static Intent newInstance(Context context, String path){
        Intent intent=new Intent(context,ShowPhotoActivity.class);
        intent.putExtra(BITMAP,path);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        ButterKnife.bind(this);
        path=getIntent().getStringExtra(BITMAP);
        mBitmap=PictureUtils.getScaledBitmap(path,ShowPhotoActivity.this);
        if (path==null){
            ivShowPhotoPicture.setImageBitmap(null);
        }else {
            ivShowPhotoPicture.setImageBitmap(mBitmap);
        }
        mPhotoViewAttacher=new PhotoViewAttacher(ivShowPhotoPicture);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBitmap.recycle();
    }
}
