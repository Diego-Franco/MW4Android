package com.defch.mw4android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.defch.mw4android.Environment;

/**
 * Created by DiegoFranco on 3/17/15.
 */
public class DBSQLite extends SQLiteOpenHelper {


    private static final String CREATE_BDD = "CREATE TABLE "
            + Environment.TABLE_SCORES + " (" + Environment._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Environment.COL_NAME + " TEXT NOT NULL,"
            + Environment.COL_TIME + " TEXT NOT NULL,"
            + Environment.COL_WIN + " TEXT NOT NULL);";

    public DBSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + Environment.TABLE_SCORES + ";");
        onCreate(db);
    }

}
