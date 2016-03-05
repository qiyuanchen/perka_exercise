package com.android.flamingo.perka_exercise;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiyuan on 3/3/2016.
 */
public class countList {
    //list of counts stored
    public static final int CONTAINER_ID=1;
    @ForeignCollectionField
    private ForeignCollection<count> counts;
    //db id, I set this to a constant by default, because we only need 1 list so far.
    @DatabaseField(id = true)
    private int id=CONTAINER_ID;
    @DatabaseField
    private int container_size;
    public countList(){}
    // returns the foreign collection as a list
    public List<count> getCounts(){
        List<count> result=new ArrayList<>();
        container_size=0;
        for(count c:counts){
            result.add(c);
        }
        return result;
    }
    public void setCounts(){

    }
    public void add(){
        container_size++;
    }
    public void sub(){
        container_size--;
    }
    public int getSize(){
        return container_size;
    }
}
