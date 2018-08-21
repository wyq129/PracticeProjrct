package com.zither.aiiage.practiceproject.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zither.aiiage.practiceproject.sqlLite.CrimeBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wangyanqin
 * @date 2018/08/10
 */
public class DatebaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatebaseHelper";
    private static final String DATABASE_NAME = "crime.db";
    public static final String TABLE_NAME = "crime_table";
    private static final int VERSION = 13;
    /**
     * Columns
     */
    public static final String crime_id = "ID";
    public static final String crime_title = "NAME";
    public static final String crime_date = "DATE";
    public static final String crime_solved = "SOLVED";
    public static final String crime_user = "USER";
    /**
     * select type
     */
    private final int COL_TYPE_ID = 1;
    private final int Col_TYPE_NAME = 2;

    public DatebaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createDatebase = "create table " + TABLE_NAME
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + crime_title + " TEXT,"
                + crime_date + " TEXT,"
                + crime_solved + " TEXT,"
                + crime_user + " TEXT)";

        sqLiteDatabase.execSQL(createDatebase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /**
     * insert crimeBean
     *
     * @param crimeBean
     * @return
     */
    public void insertCrime(CrimeBean crimeBean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(crime_title, crimeBean.getName());
        contentValues.put(crime_date, crimeBean.getDate().getTime());
        contentValues.put(crime_solved, crimeBean.isSolved());
        contentValues.put(crime_user, crimeBean.getUser());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    /**
     * update crimeBean
     *
     * @param crimeBean
     * @return
     */
    public void updateCrime(CrimeBean crimeBean) {
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(crime_id, crimeBean.getId());
        contentValues.put(crime_title, crimeBean.getName());
        contentValues.put(crime_date, crimeBean.getDate().getTime());
        contentValues.put(crime_solved, crimeBean.isSolved());
        contentValues.put(crime_user, crimeBean.getUser());
        //修改条件
        String whereClause = "ID = ?";
        //修改条件的参数
        String[] whereArgs = {crimeBean.getId() + ""};
        db.update(TABLE_NAME, contentValues, whereClause ,whereArgs );
        db.close();
    }

    /**
     * delete crimeBean
     *
     * @param id
     * @return
     */
    public boolean deleteCrimeById(int id) {
        if (id == -1) {
            return false;
        }
        return queryCrimeBy(id) != null && deleteCrime(id);

    }

    private boolean deleteCrime(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id + ""}) > 0;
    }

    /**
     * getAllCrimeBean
     *
     * @return
     */
    public List<CrimeBean> getAllCrimeBean() {
        List<CrimeBean> list = new ArrayList<>();
        String sql = "select * from " + TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        // 始终让cursor指向数据库表的第1行记录
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            CrimeBean crimeBean = new CrimeBean();
            String name = cursor.getString(cursor.getColumnIndex(crime_title));
            long date = cursor.getLong(cursor.getColumnIndex(crime_date));
            int solved = cursor.getInt(cursor.getColumnIndex(DatebaseHelper.crime_solved));
            String user=cursor.getString(cursor.getColumnIndex(crime_user));
            crimeBean.setId(cursor.getInt(0));
            crimeBean.setName(name);
            crimeBean.setDate(new Date(date));
            crimeBean.setSolved(solved != 0);
            crimeBean.setUser(user);
            list.add(crimeBean);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * getCrimeBeanById
     *
     * @param id
     * @return
     */
    public CrimeBean getCrimeBeanById(int id) {
        return queryCrimeBy(id);
    }

    private CrimeBean queryCrimeBy(int param) {
        CrimeBean crimeBean = new CrimeBean();
        if (param == -1) {
            return crimeBean;
        }
        String sql = "select * from " +TABLE_NAME;
        sql += " where ID=" + param;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        String name=cursor.getString(cursor.getColumnIndex(crime_title));
        long date=cursor.getLong(cursor.getColumnIndex(crime_date));
        int solved=cursor.getInt(cursor.getColumnIndex(crime_solved));
        String user=cursor.getString(cursor.getColumnIndex(crime_user));
        crimeBean.setId(cursor.getInt(0));
        crimeBean.setName(name);
        crimeBean.setDate(new Date(date));
        crimeBean.setSolved(solved!=0);
        crimeBean.setUser(user);
        cursor.close();
        return crimeBean;
    }
}
