package com.db;

import java.util.List;

import android.content.Context;

public class SQLitePointDataLoader extends AbstractDataLoader<List<Point>> {
	private DataSource<Point> mDataSource;
	private String mSelection;
	private String[] mSelectionArgs;
	private String mGroupBy;
	private String mHaving;
	private String mOrderBy;

	public SQLitePointDataLoader(Context context, DataSource dataSource,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		super(context);
		mDataSource = dataSource;
		mSelection = selection;
		mSelectionArgs = selectionArgs;
		mGroupBy = groupBy;
		mHaving = having;
		mOrderBy = orderBy;
	}

	@Override
	protected List<Point> buildList() {
		List<Point> testList = mDataSource.read(mSelection, mSelectionArgs,
				mGroupBy, mHaving, mOrderBy);
		return testList;
	}

	public void insert(Point entity) {
		new InsertTask(this).execute(entity);
	}

	public void update(Point entity) {
		new UpdateTask(this).execute(entity);
	}

	public void delete(Point entity) {
		new DeleteTask(this).execute(entity);
	}

	private class InsertTask extends ContentChangingTask<Point, Void, Void> {
		InsertTask(SQLitePointDataLoader loader) {
			super(loader);
		}

		@Override
		protected Void doInBackground(Point... params) {
			mDataSource.insert(params[0]);
			return (null);
		}
	}

	private class UpdateTask extends ContentChangingTask<Point, Void, Void> {
		UpdateTask(SQLitePointDataLoader loader) {
			super(loader);
		}

		@Override
		protected Void doInBackground(Point... params) {
			mDataSource.update(params[0]);
			return (null);
		}
	}

	private class DeleteTask extends ContentChangingTask<Point, Void, Void> {
		DeleteTask(SQLitePointDataLoader loader) {
			super(loader);
		}

		@Override
		protected Void doInBackground(Point... params) {
			mDataSource.delete(params[0]);
			return (null);
		}
	}
}