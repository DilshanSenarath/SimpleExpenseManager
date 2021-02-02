package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.schema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SimpleExpenseManagerDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "180582h.db";

    public SimpleExpenseManagerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        for (String sqlQuery : SimpleExpenseManagerContract.SQL_CREATE_ENTRIES) {
            db.execSQL(sqlQuery);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String sqlQuery : SimpleExpenseManagerContract.SQL_DELETE_ENTRIES) {
            db.execSQL(sqlQuery);
        }
        onCreate(db);
    }

}
