package com.android.flamingo.perka_exercise;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by Qiyuan on 3/5/2016.
 */
public class getdb {
    // static methods to return the corresponding class db
    public static Dao<countList,Integer> getCountListDB(Context context) throws SQLException {
        return databasehelper.getInstance(context).getcountListDao();
    }
    public static Dao<count,Integer> getCountDB(Context context) throws SQLException {
        return databasehelper.getInstance(context).getcountDao();
    }
}
