package com.defch.mw4android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.defch.mw4android.Environment;
import com.defch.mw4android.model.Score;

/**
 * Created by DiegoFranco on 3/17/15.
 */
public class ScoreDB {

    private static final int VERSION_BDD = 2;
    private static final String NAME_BDD = "scores.db";


    private SQLiteDatabase bdd;

    private DBSQLite myBDDSQLite;

    public ScoreDB(Context context) {
        myBDDSQLite = new DBSQLite(context, NAME_BDD, null, VERSION_BDD);
    }

    public void open() {
        bdd = myBDDSQLite.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertScore(Score scores){
        ContentValues values = new ContentValues();
        values.put(Environment.COL_NAME, scores.getUsername());
        values.put(Environment.COL_TIME, scores.getTime());
        values.put(Environment.COL_WIN, scores.getWinner());

        return bdd.insert(Environment.TABLE_SCORES, null, values);
    }

    public Cursor getAllScores(){
        Cursor mCursor = bdd.query(Environment.TABLE_SCORES, new String[] {Environment._ID,
                        Environment.COL_NAME, Environment.COL_TIME, Environment.COL_WIN},
                null, null, null, null, Environment._ID + " DESC");

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
