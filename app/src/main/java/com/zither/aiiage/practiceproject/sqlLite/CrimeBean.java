package com.zither.aiiage.practiceproject.sqlLite;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author wangyanqin
 * @date 2018/08/10
 */
public class CrimeBean implements Serializable{
    private int id;
    private String name;
    private Date mDate;
    private boolean solved;
    private String user;
    public CrimeBean(){
        //this(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return "CrimeBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", mDate=" + mDate +
                ", solved=" + solved +
                ", user='" + user + '\'' +
                '}';
    }

    public CrimeBean(int id){
       id=id;
        mDate=new Date();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
    /**
     * 获取照片文件名
     */
    public String getPhotoFilename(){
        return "IMG_"+getId()+".jpg";
    }
}
