package com.zither.aiiage.practiceproject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zither.aiiage.practiceproject.sqlLite.CrimeBean;
import com.zither.aiiage.practiceproject.sqlLite.DatebaseHelper;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangyanqin
 * @date 2018/08/10
 */
public class MainActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.imageview_show)
    ImageView imageviewShow;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.et_main_inputTitle)
    EditText etMainInputTitle;
    @BindView(R.id.button_show_date)
    Button buttonShowDate;
    @BindView(R.id.cb_ifsolved)
    CheckBox cbIfsolved;
    @BindView(R.id.button_select_user)
    Button buttonSelectUser;
    @BindView(R.id.button_send)
    Button buttonSend;
    DatebaseHelper mHelper=new DatebaseHelper(MainActivity.this);
    CrimeBean mCrimeBean;
    boolean isSovled=false;
    private static final String TAG="MainActivity";
    private static final int REQUEST_CODE=1;
    Intent pickContact=new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mCrimeBean=new CrimeBean();
        buttonShowDate.setText(new Date().toString());
        buttonSend.setOnClickListener(this);
        buttonSelectUser.setOnClickListener(this);
        cbIfsolved.setOnCheckedChangeListener(this);
        pickContact.addCategory(Intent.CATEGORY_HOME);
        PackageManager manager=MainActivity.this.getPackageManager();
        if (manager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY)==null){
            buttonSelectUser.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_send:
                String title=etMainInputTitle.getText().toString();
                Date date=new Date();
                mCrimeBean.setName(title);
                mCrimeBean.setDate(date);
                mCrimeBean.setSolved(isSovled);
                mHelper.insertCrime(mCrimeBean);
                Log.d(TAG, "onClick: "+mCrimeBean.toString());
                Intent data=new Intent();
                data.putExtra("Title",mCrimeBean);
                setResult(2,data);
                finish();
                break;
            case R.id.button_select_user:
                //获取联系人
                startActivityForResult(pickContact,REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (isSovled){
            mCrimeBean.setSolved(isSovled);
        }else {
            isSovled=true;
            mCrimeBean.setSolved(isSovled);
        }
        cbIfsolved.setChecked(isSovled);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!=Activity.RESULT_OK){
            return;
        }
        if (requestCode==REQUEST_CODE&&data!=null){
            Uri uri=data.getData();
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor cursor = MainActivity.this.getContentResolver().query(uri, queryFields, null, null, null);
            try {
                if (cursor.getCount()==0){
                    return;
                }
                cursor.moveToFirst();
                String user=cursor.getString(0);
                mCrimeBean.setUser(user);
                buttonSelectUser.setText("联系人是："+user);
            } finally {
                cursor.close();
            }
        }
    }
}
