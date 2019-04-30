package com.example.ilham.vehiclehouse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db_parkir_user";
    private static final String TABLE_NAME = "users";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "( id_user TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addRecord(String id){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_user", id);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int countRecord(){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT count(*) FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        int cek;
        if (count == 0){
            cek = 0;
        }else{
            cek = 1;
        }

        return cek;
    }

    public String select(){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        String id_user = cursor.getString(0);

        return id_user;
    }

    public void truncate(){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_NAME;
        db.execSQL(sql);
        db.close();
    }
}
