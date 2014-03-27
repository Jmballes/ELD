package com.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract.Columns;

public class PointDataSource extends DataSource<Point> {
	public static final String TABLE_NAME = "test";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_POINTDATE = "pointdate";
	public static final String COLUMN_POINTS = "points";

	// Database creation sql statement
	public static final String CREATE_COMMAND = "create table " +
	TABLE_NAME
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_NAME + " text not null," 
			+ COLUMN_POINTDATE + " numeric ,"
			+ COLUMN_POINTS + " integer default 0 );";

	public PointDataSource(SQLiteDatabase database) {
		super(database);
	}

	@Override
	public boolean insert(Point entity) {
		if (entity == null) {
			return false;
		}
		long result = mDatabase.insert(TABLE_NAME, null,
				generateContentValuesFromObject(entity));
		return result != -1;
	}

	@Override
	public boolean delete(Point entity) {
		if (entity == null) {
			return false;
		}
		int result = mDatabase.delete(TABLE_NAME,
				COLUMN_ID + " = " + entity.getId(), null);
		return result != 0;
	}

	@Override
	public boolean update(Point entity) {
		if (entity == null) {
			return false;
		}
		int result = mDatabase.update(TABLE_NAME,
				generateContentValuesFromObject(entity), COLUMN_ID + " = "
						+ entity.getId(), null);
		return result != 0;
	}

	@Override
	public List<Point> read() {
		System.out.println(TABLE_NAME);
		System.out.println(getAllColumns());
		Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), null,
				null, null, null, null);
		List tests = new ArrayList();
		if (cursor != null && cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				tests.add(generateObjectFromCursor(cursor));
				cursor.moveToNext();
			}
			cursor.close();
		}
		return tests;
	}

	@Override
	public List<Point> read(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), selection,
				selectionArgs, groupBy, having, orderBy);
		List tests = new ArrayList();
		if (cursor != null && cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				tests.add(generateObjectFromCursor(cursor));
				cursor.moveToNext();
			}
			cursor.close();
		}
		return tests;
	}

	public String[] getAllColumns() {
		return new String[] { COLUMN_ID, COLUMN_NAME, COLUMN_POINTDATE,
				COLUMN_POINTS ,};
	}

	public Point generateObjectFromCursor(Cursor cursor) {
		if (cursor == null) {
			return null;
		}
		Point point = new Point();
		point.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
		point.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
		point.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_POINTDATE)));

		point.setPoints(cursor.getInt(cursor.getColumnIndex(COLUMN_POINTS)));
		return point;
	}

	public ContentValues generateContentValuesFromObject(Point entity) {
		if (entity == null) {
			return null;
		}
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, entity.getName());
		return values;
	}

}