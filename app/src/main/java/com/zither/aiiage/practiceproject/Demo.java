package com.zither.aiiage.practiceproject;

import com.zither.aiiage.practiceproject.sqlLite.CrimeBean;

import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Demo<E> {
    HashMap<Integer,CrimeBean> users=new HashMap<>();
  //  users.put(1,new CrimeBean(1))

    public static HashMap<Integer,CrimeBean> sortHashMap(final HashMap<Integer,CrimeBean> hashMap){
        //首先拿到map键值对集合
        Set<Map.Entry<Integer,CrimeBean>> entrySet1=hashMap.entrySet();
        //将Set集合转为List集合，为了使用工具类的排序算法
        List<Map.Entry<Integer,CrimeBean>> list=new ArrayList<Map.Entry<Integer,CrimeBean>>(entrySet1);
        //使用工具类Collections集合工具类对list进行排序
        Collections.sort(list, new Comparator<Map.Entry<Integer, CrimeBean>>() {
            @Override
            public int compare(Map.Entry<Integer, CrimeBean> integerCrimeBeanEntry, Map.Entry<Integer, CrimeBean> t1) {
                //倒序排序
                Collections.synchronizedMap(hashMap);
                return t1.getValue().getId()-integerCrimeBeanEntry.getValue().getId();
            }
        });
        //创建一个新的有序的HashMap子类的集合
        LinkedHashMap<Integer,CrimeBean> linkedHashMap=new LinkedHashMap<Integer, CrimeBean>();
        //将list中的数据存在LinkedHashMap中
        for (Map.Entry<Integer,CrimeBean> crimeBeanEntry:list){
            linkedHashMap.put(crimeBeanEntry.getKey(),crimeBeanEntry.getValue());
        }
        //返回结果
        return linkedHashMap;
    }
}
