package com.zither.aiiage.practiceproject;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.zither.aiiage.practiceproject.sqlLite.CrimeBean;
import com.zither.aiiage.practiceproject.sqlLite.DatebaseHelper;

import java.io.File;
import java.util.Date;
import java.util.List;

import cn.bluemobi.dylan.photoview.library.PhotoViewAttacher;


/**
 * @author wangyanqin
 */
public class CrimePagerFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private static final String DATE = "date";
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO = 2;
    CrimeBean mCrimeBean;
    private EditText mTitle;
    private CheckBox mCheckBox;
    private Button mShowDate, updateDate, selectUser, sendData;
    int crimeId;
    String user;
    final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
    private File mPhotoFile;
    private ImageButton takePhoto;
    private ImageView imageView_show;
    final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    //第三方开源库：PhotoView-master实现缩放的效果 compile 添加
    private PhotoViewAttacher mPhotoViewAttacher;

    public static CrimePagerFragment newInstance(int crimeId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATE, crimeId);
        CrimePagerFragment crimePagerFragment = new CrimePagerFragment();
        crimePagerFragment.setArguments(bundle);
        return crimePagerFragment;
    }

    Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_pager, container, false);
        mTitle = view.findViewById(R.id.crimePage_inputTitle);
        mCheckBox = view.findViewById(R.id.crimePage_cb_ifsolved);
        mShowDate = view.findViewById(R.id.crimePage_button_show_date);
        updateDate = view.findViewById(R.id.crimePage_button_send);
        selectUser = view.findViewById(R.id.crimePage_button_select_user);
        sendData = view.findViewById(R.id.crimePage_button_sendto);
        takePhoto = view.findViewById(R.id.crimePage_takePhoto);
        imageView_show = view.findViewById(R.id.crimePage_imageView_show);
        sendData.setOnClickListener(this);
        selectUser.setOnClickListener(this);
        updateDate.setOnClickListener(this);
        PackageManager manager = getActivity().getPackageManager();
        if (manager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            selectUser.setEnabled(false);
        }
        Bundle b = getArguments();
        crimeId = b.getInt(DATE, -1);
        Log.d(DATE, "onCreateView: " + crimeId);
        DatebaseHelper datebaseHelper = new DatebaseHelper(getActivity());
        mCrimeBean = datebaseHelper.getCrimeBeanById(crimeId);
        //获取照片文件位置
        mPhotoFile = datebaseHelper.getPhotoFile(mCrimeBean, getActivity());
        user = mCrimeBean.getUser();
        mTitle.setText(mCrimeBean.getName());
        mCheckBox.setChecked(mCrimeBean.isSolved());
        mShowDate.setText(mCrimeBean.getDate().toString());
        updatePhotoView();
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrimeBean.setSolved(b);
            }
        });
        if (mCrimeBean.getUser() != null) {
            selectUser.setText("联系人是：" + mCrimeBean.getUser());
        }
        //触发拍照，使用相机Intent
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = getActivity().getPackageManager();
        //检查是否有响应式相机
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        takePhoto.setEnabled(canTakePhoto);
        takePhoto.setOnClickListener(this);
       imageView_show.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.crimePage_button_send:
                String Title = mTitle.getText().toString();
                Date date = new Date();
                mCrimeBean.setId(crimeId);
                mCrimeBean.setName(Title);
                mCrimeBean.setDate(date);
                mCrimeBean.setUser(user);
                DatebaseHelper m = new DatebaseHelper(getActivity());
                m.updateCrime(mCrimeBean);
                Intent intent = new Intent(getActivity(), CrimeListActivity.class);
                startActivity(intent);
                break;
            case R.id.crimePage_button_select_user:
                startActivityForResult(pickContact, REQUEST_CONTACT);
                break;
            case R.id.crimePage_button_sendto:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.crime_report));
                startActivity(i);
                break;
            case R.id.crimePage_takePhoto:
                //把本地文件路径转换成相机能看见的Uri形式
                Uri uri = FileProvider.getUriForFile(getActivity(), "com.bignerdranch.android.criminaLintent.fileprovider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                //给相机应用权限
                List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : list) {
                    getActivity().grantUriPermission(resolveInfo.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
                break;
            case R.id.crimePage_imageView_show:
                bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
                Intent intent1 = ShowPhotoActivity.newInstance(getActivity(),mPhotoFile.getPath());
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrimeBean.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = android.text.format.DateFormat.format(dateFormat, mCrimeBean.getDate()).toString();
        String user = mCrimeBean.getUser();
        if (user == null) {
            user = getString(R.string.crime_report_no_user);
        } else {
            user = getString(R.string.crime_report_user, user);
        }
        @SuppressLint("StringFormatMatches")
        String report = getString(R.string.crime_report_user, mCrimeBean.getUser(), dateString, solvedString, user);
        return report;
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            imageView_show.setImageBitmap(null);
        } else {
            bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());

            imageView_show.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor cursor = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
            try {
                if (cursor.getCount() == 0) {
                    return;
                }
                cursor.moveToFirst();
                user = cursor.getString(0);
                // mCrimeBean.setUser(user);
                selectUser.setText("联系人是：" + user);
            } finally {
                cursor.close();
            }
        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.bignerdranch.android.criminaLintent.fileprovider", mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     *
     */
    public interface ICallBack{
        /**
         * 发送bitmap到Activity
         * @param bitmap
         */
        void sendBitmap(Bitmap bitmap);
    }
    // 设置 接口回调 方法
    public void sendMessage(ICallBack callBack){
        callBack.sendBitmap(bitmap);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bitmap.recycle();
    }
}
