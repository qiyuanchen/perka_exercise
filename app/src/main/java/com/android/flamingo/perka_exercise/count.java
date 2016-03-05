package com.android.flamingo.perka_exercise;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * Created by Qiyuan on 3/3/2016.
 */
public class count {

    //database id
    @DatabaseField(generatedId = true)
    private int id;
    //connects count instances to countlist in db
    @DatabaseField(foreign = true)
    private countList countlist;
    // for lazy deletion
    @DatabaseField
    private boolean deleted;
    //counter number
    @DatabaseField
    private int num;
    //counter name
    @DatabaseField
    private String name;
    @DatabaseField
    private int position;
    public count(){


    }
    public count(countList cl, String name,int position){
        this.num=0;
        this.deleted=false;
        this.countlist=cl;
        this.name=name;
        this.position=position;
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
    public String getName(){return name;}
    public int getid(){return id;}
    public void setName(String name){this.name=name;}
    public void delete(){
        deleted=true;
    }
    public void updatePosition(int p){
        this.position=p;
    }
    public int getPosition(){
        return position;
    }




}
