package com.zither.aiiage.practiceproject.sqlLite;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;

/**
 * @author wangyanqin
 * @date 2018/08/11
 */
public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public CrimeBean getCrimeBean() {
        String titel = getString(getColumnIndex(DatebaseHelper.crime_title));
        long date = getLong(getColumnIndex(DatebaseHelper.crime_date));
        int isolved = getInt(getColumnIndex(DatebaseHelper.crime_solved));
        String user = getString(getColumnIndex(DatebaseHelper.crime_user));
        CrimeBean crimeBean = new CrimeBean();
        crimeBean.setName(titel);
        crimeBean.setDate(new Date(date));
        crimeBean.setSolved(isolved != 0);
        crimeBean.setUser(user);
        return crimeBean;
    }
}
