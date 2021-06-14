package com.hc.mixthebluetooth.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hc.mixthebluetooth.bean.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SqliteDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "sqlite_dbname";
    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static SqliteDB sqliteDB;

    private SQLiteDatabase db;

    private SqliteDB(Context context) {
        OpenHelper dbHelper = new OpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取SqliteDB实例
     * @param context
     */
    public synchronized static SqliteDB getInstance(Context context) {
        if (sqliteDB == null) {
            sqliteDB = new SqliteDB(context);
        }
        return sqliteDB;
    }

    /**
     * 将User实例存储到数据库。
     */
    public int  saveUser(User user) {
        if (user != null) {
           /* ContentValues values = new ContentValues();
            values.put("username", user.getUsername());
            values.put("userpwd", user.getUserpwd());
            db.insert("User", null, values);*/

            Cursor cursor = db.rawQuery("select * from User where name=?", new String[]{user.getName()});
            if (cursor.getCount() > 0) {
                return -1;
            } else {
                try {
                    db.execSQL("insert into User(name,sex,age,weight) values(?,?,?,?) ", new String[]{user.getName(), user.getSex(), String.valueOf(user.getAge()), String.valueOf(user.getWeight())});
                } catch (Exception e) {
                    Log.d("错误", e.getMessage().toString());
                    return 2;
                }
                return 1;
            }
        }else {
            return 0;
        }
    }

    /**
     * 从数据库读取User信息。
     */
    public ArrayList<User> loadUser() {
        ArrayList<User> list = new ArrayList<User>();
        Cursor cursor = db
                .query("User", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setName(cursor.getString(cursor.getColumnIndex("name")));
                user.setSex(cursor.getString(cursor.getColumnIndex("sex")));
                user.setAge(cursor.getInt(cursor.getColumnIndex("age")));
                user.setWeight(cursor.getDouble(cursor.getColumnIndex("weight")));
                list.add(user);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public int Quer(String pwd, String name)
    {


        HashMap<String, String> hashmap=new HashMap<String, String>();
        Cursor cursor =db.rawQuery("select * from User where name=?", new String[]{name});

        // hashmap.put("name",db.rawQuery("select * from User where name=?",new String[]{name}).toString());
        if (cursor.getCount()>0)
        {
            Cursor pwdcursor =db.rawQuery("select * from User where userpwd=? and username=?",new String[]{pwd,name});
            if (pwdcursor.getCount()>0)
            {
                return 1;
            }
            else {
                return -1;
            }
        }
        else {
            return 0;
        }


    }
}
