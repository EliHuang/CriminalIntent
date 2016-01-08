package com.eli.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eli.android.criminalintent.database.CrimeBaseHelper;
import com.eli.android.criminalintent.database.CrimeCursorWrapper;
import com.eli.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    //private ArrayList<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDataBase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        //mCrimes = new ArrayList<>();
        /*
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
        }
        */
        mContext = context.getApplicationContext();
        mDataBase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getContentValues (Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1: 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return values;
    }

    public void addCrime(Crime c) {
        //mCrimes.add(c);
        ContentValues values = getContentValues(c);
        mDataBase.insert(CrimeTable.NAME, null, values);
    }

    public void deleteCrime(Crime c) {
        //ContentValues values = getContentValues(c);
        String uuidString = c.getId().toString();

        mDataBase.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void updateCrime (Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDataBase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void swapCrime(Crime source, Crime target) {
        ContentValues v1 = getContentValues(source);
        ContentValues v2 = getContentValues(target);

        //v1.put(CrimeTable.Cols.UUID, v2.get(CrimeTable.Cols.UUID).toString());
        v1.put(CrimeTable.Cols.TITLE, target.getTitle());
        v1.put(CrimeTable.Cols.DATE, target.getDate().getTime());
        v1.put(CrimeTable.Cols.SOLVED, target.isSolved() ? 1: 0);
        v1.put(CrimeTable.Cols.SUSPECT, target.getSuspect());

        v2.put(CrimeTable.Cols.TITLE, source.getTitle());
        v2.put(CrimeTable.Cols.DATE, source.getDate().getTime());
        v2.put(CrimeTable.Cols.SOLVED, source.isSolved() ? 1 : 0);
        v1.put(CrimeTable.Cols.SUSPECT, source.getSuspect());

        mDataBase.update(CrimeTable.NAME, v1,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{source.getId().toString()});
        mDataBase.update(CrimeTable.NAME, v2,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{target.getId().toString()});
    }

    private CrimeCursorWrapper queryCrimes (String whereClause, String[] whereArgs) {
        Cursor cursor = mDataBase.query(
                CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new CrimeCursorWrapper(cursor);
    }

    public List<Crime> getCrimes() {

        //return mCrimes;
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            crimes.add(cursor.getCrime());
            cursor.moveToNext();
        }
        cursor.close();
        return  crimes;
    }

    public Crime getCrime(UUID id) {
        /*
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;*/
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?",
                new String[] {id.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

}
