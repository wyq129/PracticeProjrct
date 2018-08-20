package com.zither.aiiage.practiceproject.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyanqin
 * @date 2018/08/11
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    public CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mSQLiteDatabase = new DatebaseHelper(mContext).getWritableDatabase();
    }

    public void addCrimeBean(CrimeBean crimeBean) {
        new DatebaseHelper(mContext).insertCrime(crimeBean);
}
    public void updateCrimeBean(CrimeBean crimeBean){
        int uuid=crimeBean.getId();
        ContentValues contentValues=getContentValues(crimeBean);
        mSQLiteDatabase.update(DatebaseHelper.TABLE_NAME,contentValues,DatebaseHelper.crime_id+"=?",new String[]{uuid+""});
    }
    public List<CrimeBean> getCrimeBeanList() {
        List<CrimeBean> list=new ArrayList<>();
        CrimeCursorWrapper crimeCursorWrapper= (CrimeCursorWrapper) queryCrime(null,null);
        try {
            crimeCursorWrapper.moveToFirst();
            while (!crimeCursorWrapper.isAfterLast()){
                list.add(crimeCursorWrapper.getCrimeBean());
                crimeCursorWrapper.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public CrimeBean getCrimeBean(int uuid) {
        CrimeCursorWrapper cursorWrapper= (CrimeCursorWrapper) queryCrime(DatebaseHelper.crime_id+"=?",new String[]{uuid+""});
        try {
            if (cursorWrapper.getCount()==0){
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrimeBean();
        } finally {
            cursorWrapper.close();
        }
    }

    /**
     * write data to db
     * @param crimeBean
     * @return
     */
    private static ContentValues getContentValues(CrimeBean crimeBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatebaseHelper.crime_id, crimeBean.getId());
        contentValues.put(DatebaseHelper.crime_title, crimeBean.getName().toString());
        contentValues.put(DatebaseHelper.crime_date,crimeBean.getDate().getTime());
        contentValues.put(DatebaseHelper.crime_solved, crimeBean.isSolved() ? 1 : 0);
        contentValues.put(DatebaseHelper.crime_user, crimeBean.getUser().toString());
        return contentValues;
    }
    /**
     * read date form db
     */
    public CrimeCursorWrapper queryCrime(String whereClause,String[] whereArgs){
        Cursor cursor=mSQLiteDatabase.query(DatebaseHelper.TABLE_NAME,null,whereClause,whereArgs,null,null,null);
        return new CrimeCursorWrapper(cursor);
    }
}
