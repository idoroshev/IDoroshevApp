package com.yandex.android.idoroshevapp.data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.yandex.android.idoroshevapp.R;

public class Database {

    private final static String TAG = "Database";
    private static String insert;
    private static String update;
    private static String failedTo;
    private static String or;
    private static String get;
    private static String remove;
    private static String clear;

    private interface AppDatabase {
        String TABLE_NAME = "launched";

        interface Columns extends BaseColumns {
            String FIELD_NUMBER = "number";
            String FIELD_TITLE = "package_name";
        }

        String CREATE_TABLE_SCRIPT =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                        "(" +
                        Columns.FIELD_NUMBER + " NUMBER, " +
                        Columns.FIELD_TITLE + " TEXT" +
                        ")";

        String DROP_TABLE_SCRIPT =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    private static class DbHelper extends SQLiteOpenHelper {
        static final int DATABASE_VERSION = 1;
        static final String DATABASE_NAME = "applications.db";

        DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(AppDatabase.CREATE_TABLE_SCRIPT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(AppDatabase.DROP_TABLE_SCRIPT);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private static DbHelper dbHelper;

    public static void initialize(Activity activity) {
        dbHelper = new DbHelper(activity);

        Context context = activity.getApplicationContext();
        insert = context.getString(R.string.insert);
        update = context.getString(R.string.update);
        failedTo = context.getString(R.string.failed_to);
        or = context.getString(R.string.or);
        get = context.getString(R.string.get);
        remove = context.getString(R.string.remove);
        clear = context.getString(R.string.clear);
    }

    public static void insert(AppInfo appInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AppDatabase.Columns.FIELD_NUMBER, appInfo.getLaunched());
        contentValues.put(AppDatabase.Columns.FIELD_TITLE, appInfo.getPackageName());
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert(
                    AppDatabase.TABLE_NAME,
                    null,
                    contentValues
            );
        } catch (SQLiteException e) {
            Log.e(TAG, failedTo + " " + insert);
        }
    }

    public static void update(AppInfo appInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AppDatabase.Columns.FIELD_NUMBER, appInfo.getLaunched());
        contentValues.put(AppDatabase.Columns.FIELD_TITLE, appInfo.getPackageName());

        String table = AppDatabase.TABLE_NAME;
        String whereClause = AppDatabase.Columns.FIELD_TITLE + " = ?";
        String[] whereArgs =  {appInfo.getPackageName()};

        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.update(table, contentValues, whereClause, whereArgs);
        } catch (SQLiteException e) {
            Log.e(TAG, failedTo + " " + update);
        }
    }

    public static void insertOrUpdate(AppInfo appInfo) {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String table = AppDatabase.TABLE_NAME;
            String[] projection = {AppDatabase.Columns.FIELD_NUMBER};
            String selection = AppDatabase.Columns.FIELD_TITLE + " = ?";
            String[] selectionArgs = {appInfo.getPackageName()};

            Cursor cursor = db.query(
                    table,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            if (cursor.getCount() == 0) {
                insert(appInfo);
            } else {
                update(appInfo);
            }
            cursor.close();
        } catch (SQLiteException e) {
            Log.e(TAG, failedTo + " " + insert + " " + or + " " + update);
        }
    }

    public static int get(String title) {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String table = AppDatabase.TABLE_NAME;
            String[] projection = {AppDatabase.Columns.FIELD_NUMBER};
            String selection = AppDatabase.Columns.FIELD_TITLE + " = ?";
            String[] selectionArgs = {title};

            Cursor cursor = db.query(
                    table,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            int result = 0;
            while (cursor.moveToNext()) {
                result += cursor.getInt(cursor.getColumnIndex(AppDatabase.Columns.FIELD_NUMBER));
            }
            cursor.close();
            return result;
        } catch (SQLiteException e) {
            Log.e(TAG, failedTo + " " + get);
        }
        return 0;
    }

    public static void remove(AppInfo appInfo) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String table  = AppDatabase.TABLE_NAME;
            String whereClause = AppDatabase.Columns.FIELD_TITLE + " = ?";
            String[] whereArgs = {appInfo.getPackageName()};

            db.delete(table, whereClause, whereArgs);
        } catch (SQLiteException e) {
            Log.e(TAG, failedTo + " " + remove);
        }
    }

    public static void clear() {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(AppDatabase.TABLE_NAME, null, null);
            db.close();
        } catch (SQLiteException e) {
            Log.e(TAG, failedTo + " " + clear);
        }
    }

}
