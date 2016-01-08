package com.eli.android.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.eli.android.criminalintent.database.CrimeDbSchema.CrimeTable;
/**
 * Created by th26 on 2015/12/22.
 */
public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "CrimeBaseHelper";
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "crimeBase.db";

    //public CrimeBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    public CrimeBaseHelper (Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + CrimeTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        CrimeTable.Cols.UUID + ", " +
                        CrimeTable.Cols.TITLE + ", " +
                        CrimeTable.Cols.DATE + ", " +
                        CrimeTable.Cols.SUSPECT + ", " +
                        CrimeTable.Cols.SOLVED +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("alter table" + CrimeTable.NAME + "add column" +
                        //CrimeTable.Cols.SUSPECT);
    }

}