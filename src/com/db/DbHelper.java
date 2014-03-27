package com.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DbHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "test.db";
	private static final int DATABASE_VERSION = 2;
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(PointDataSource.CREATE_COMMAND);
		System.out.println(PointDataSource.CREATE_COMMAND);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + PointDataSource.TABLE_NAME);
		onCreate(db);
	}
}