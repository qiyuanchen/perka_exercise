package com.android.flamingo.perka_exercise;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Qiyuan on 3/3/2016.
 */
public class databasehelper extends OrmLiteSqliteOpenHelper {
    private static final String DATA_BASE_FILE_NAME="counter";
    //List of tables
    private final Class []tableClasses=new Class[]{countList.class,count.class};
    //db version
    private static final int dbversion=1;
    private static databasehelper instance;

    public static synchronized databasehelper getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new databasehelper(context);
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
    {
        //creates the table
        try
        {
            for(Class mTableClass : tableClasses)
            {
                TableUtils.createTable(connectionSource, mTableClass);
            }
        }
        catch(SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion){
        // codes to update db if necessary
    }

    // required default constructor
    private databasehelper(Context context){
        super(context,DATA_BASE_FILE_NAME,null,dbversion);
    }

    //returns class db
    public Dao<countList,Integer> getcountListDao() throws SQLException{
        return getDao(countList.class);
    }
    public Dao<count,Integer> getcountDao() throws SQLException{
        return getDao(count.class);
    }


}
