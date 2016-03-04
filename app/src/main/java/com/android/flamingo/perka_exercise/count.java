package com.android.flamingo.perka_exercise;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * Created by Qiyuan on 3/3/2016.
 */
public class count {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true)
    private countList countlist;
    @DatabaseField
    private boolean deleted;
    @DatabaseField
    private int num;
    public count(){


    }
    public count(countList cl){
        this.num=0;
        this.deleted=false;
        this.countlist=cl;
    }
    public void addNum(){
        num+=1;
    }
    public int getNum(){
        return num;
    }
    public void delNum(){
        num-=1;
    }
    public boolean ifDel(){
        return deleted;
    }



}
