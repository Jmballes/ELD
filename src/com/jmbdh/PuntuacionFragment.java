package com.jmbdh;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.db.DbHelper;
import com.db.SQLitePointDataLoader;
import com.db.Point;
import com.db.PointDataSource;
import com.db.PointListAdapter;


public class PuntuacionFragment extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DbHelper helper = new DbHelper(this);
		SQLiteDatabase database = helper.getWritableDatabase();
		PointDataSource dataSource = new PointDataSource(database);
		List list2 = dataSource.read();
		// if(list2 == null || list2.size() == 0){
		dataSource.insert(new Point("Tom"));
		dataSource.insert(new Point("Dick"));
		dataSource.insert(new Point("Harry"));
		// }
		database.close();
		// Create the ListFragment and add it as our sole content.
		FragmentManager fm = getSupportFragmentManager();
		if (fm.findFragmentById(android.R.id.content) == null) {
			AppListFragment list = new AppListFragment();
			fm.beginTransaction().add(android.R.id.content, list).commit();
		}
	}

	/**
	 * This ListFragment displays a list of all installed applications on the
	 * device as its sole content. It uses an {@link AppListLoader} to load its
	 * data and the LoaderManager to manage the loader across the activity and
	 * fragment life cycles.
	 */
	public static class AppListFragment extends ListFragment implements
			LoaderManager.LoaderCallbacks<List<Point>> {
		private static final String TAG = "ADP_AppListFragment";
		private static final boolean DEBUG = true;

		// The Loader's id (this id is specific to the ListFragment's
		// LoaderManager)
		private static final int LOADER_ID = 1;
		private SQLiteDatabase mDatabase;
		private PointDataSource mDataSource;
		private DbHelper mDbHelper;
		private PointListAdapter mAdapter;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			// setHasOptionsMenu(true);
			mDbHelper = new DbHelper(getActivity());
			mDatabase = mDbHelper.getWritableDatabase();
			mDataSource = new PointDataSource(mDatabase);
			mAdapter = new PointListAdapter(getActivity());
			setEmptyText("No data, please add from menu.");
			setListAdapter(mAdapter);
			setListShown(false);
			if (DEBUG) {
				Log.i(TAG, "+++ Calling initLoader()! +++");
				if (getLoaderManager().getLoader(LOADER_ID) == null) {
					Log.i(TAG, "+++ Initializing the new Loader... +++");
				} else {
					Log.i(TAG,
							"+++ Reconnecting with existing Loader (id '1')... +++");
				}
			}
			// Initialize a Loader with id '1'. If the Loader with this id
			// already
			// exists, then the LoaderManager will reuse the existing Loader.
			getLoaderManager().initLoader(LOADER_ID, null, this);
		}

		/**********************/
		/** LOADER CALLBACKS **/
		/**********************/

		@Override
		public Loader<List<Point>> onCreateLoader(int id, Bundle args) {
			SQLitePointDataLoader loader = new SQLitePointDataLoader(
					getActivity(), mDataSource, null, null, null, null, null);
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<List<Point>> loader, List<Point> data) {
			if (DEBUG)
				Log.i(TAG, "+++ onLoadFinished() called! +++");
			mAdapter.clear();
			mAdapter.setData(data);
			if (isResumed()) {
				setListShown(true);
			} else {
				setListShownNoAnimation(true);
			}
		}

		@Override
		public void onLoaderReset(Loader<List<Point>> loader) {
			mAdapter.clear();
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			mDbHelper.close();
			mDatabase.close();
			mDataSource = null;
			mDbHelper = null;
			mDatabase = null;
		}
	}
}

