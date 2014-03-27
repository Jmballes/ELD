package com.db;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

public abstract class DataSource<T> {
	protected SQLiteDatabase mDatabase;

	public DataSource(SQLiteDatabase database) {
		mDatabase = database;
	}

	public abstract boolean insert(T entity);

	public abstract boolean delete(T entity);

	public abstract boolean update(T entity);

	public abstract List read();

	public abstract List read(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy);
}
