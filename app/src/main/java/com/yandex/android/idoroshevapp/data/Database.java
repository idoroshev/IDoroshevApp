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

    private interface LaunchedTable {
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

    private interface DesktopTable {
        String TABLE_NAME = "desktop";

        interface Columns extends BaseColumns {
            String FIELD_TITLE = "package_name";
        }

        String CREATE_TABLE_SCRIPT =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                        "(" +
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
            db.execSQL(LaunchedTable.CREATE_TABLE_SCRIPT);
            db.execSQL(DesktopTable.CREATE_TABLE_SCRIPT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(LaunchedTable.DROP_TABLE_SCRIPT);
            db.execSQL(DesktopTable.DROP_TABLE_SCRIPT);
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

    public static void insertLaunched(AppInfo appInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LaunchedTable.Columns.FIELD_NUMBER, appInfo.getLaunched());
        contentValues.put(LaunchedTable.Columns.FIELD_TITLE, appInfo.getPackageName());
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert(
                    LaunchedTable.TABLE_NAME,
                    null,
                    contentValues
            );
        } catch (SQLiteException e) {
            Log.e(TAG, failedTo + " " + insert);
        }
    }

    public static void updateLaunched(AppInfo appInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LaunchedTable.Columns.FIELD_NUMBER, appInfo.getLaunched());
        contentValues.put(LaunchedTable.Columns.FIELD_TITLE, appInfo.getPackageName());

        String table = LaunchedTable.TABLE_NAME;
        String whereClause = LaunchedTable.Columns.FIELD_TITLE + " = ?";
        String[] whereArgs =  {appInfo.getPackageName()};

        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.update(table, contentValues, whereClause, whereArgs);
        } catch (SQLiteException e) {
            Log.e(TAG, failedTo + " " + update);
        }
    }

    public static void insertOrUpdateLaunched(AppInfo appInfo) {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String table = LaunchedTable.TABLE_NAME;
            String[] projection = {LaunchedTable.Columns.FIELD_NUMBER};
            String selection = LaunchedTable.Columns.FIELD_TITLE + " = ?";
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
                insertLaunched(appInfo);
            } else {
                updateLaunched(appInfo);
            }
            cursor.close();
        } catch (SQLiteException e) {
            Log.e(TAG, failedTo + " " + insert + " " + or + " " + update);
        }
    }

    public static int getLaunched(String title) {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String table = LaunchedTable.TABLE_NAME;
            String[] projection = {LaunchedTable.Columns.FIELD_NUMBER};
            String selection = LaunchedTable.Columns.FIELD_TITLE + " = ?";
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
                result += cursor.getInt(cursor.getColumnIndex(LaunchedTable.Columns.FIELD_NUMBER));
            }
            cursor.close();
            return result;
        } catch (SQLiteException e) {
            Log.e(TAG, failedTo + " " + get);
        }
        return 0;
    }

    public static void removeLaunched(AppInfo appInfo) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String table  = LaunchedTable.TABLE_NAME;
            String whereClause = LaunchedTable.Columns.FIELD_TITLE + " = ?";
            String[] whereArgs = {appInfo.getPackageName()};

            db.delete(table, whereClause, whereArgs);
        } catch (SQLiteException e) {
            Log.e(TAG, failedTo + " " + remove);
        }
    }

    public static void clearLaunched() {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(LaunchedTable.TABLE_NAME, null, null);
            db.close();
        } catch (SQLiteException e) {
            Log.e(TAG, failedTo + " " + clear);
        }
    }

    public static boolean containsOnDesktop(AppInfo appInfo) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String table = DesktopTable.TABLE_NAME;
        String[] projection = {DesktopTable.Columns.FIELD_TITLE};
        String selection = LaunchedTable.Columns.FIELD_TITLE + " = ?";
        String[] selectionArgs = {appInfo.getPackageName()};
        Cursor cursor = db.query(table, projection, selection, selectionArgs, null, null, null);

        if (cursor.isAfterLast()) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    public static void addToDesktop(AppInfo appInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DesktopTable.Columns.FIELD_TITLE, appInfo.getPackageName());
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert(
                    DesktopTable.TABLE_NAME,
                    null,
                    contentValues
            );
        } catch (SQLiteException e) {
            Log.e(TAG, failedTo + " " + insert);
        }
    }

    public static void removeFromDesktop(AppInfo appInfo) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String table  = DesktopTable.TABLE_NAME;
            String whereClause = DesktopTable.Columns.FIELD_TITLE + " = ?";
            String[] whereArgs = {appInfo.getPackageName()};

            db.delete(table, whereClause, whereArgs);
        } catch (SQLiteException e) {
            Log.e(TAG, failedTo + " " + remove);
        }
    }
}
