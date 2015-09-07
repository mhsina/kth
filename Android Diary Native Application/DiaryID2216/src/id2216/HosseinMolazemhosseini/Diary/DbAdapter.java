package id2216.HosseinMolazemhosseini.Diary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NOTE = "note";
	public static final String KEY_IMAGELINK = "imagelink";
	public static final String KEY_TAG = "tag";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_STARTDATE = "startdate";
	public static final String KEY_ENDDATE = "enddate";
	private static final String TAG = "DbAdapter";

	private static final String DATABASE_NAME = "diarydb";
	private static final String DATABASE_TABLE = "diarytb";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table diarytb (_id integer primary key autoincrement, "
			+ "note text not null, imagelink text, "
			+ "tag text, address text, startdate text, enddate text);";
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DbAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS diarytb");
			onCreate(db);
		}
	}

	// ---opens the database---
	public DbAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// ---insert a row into the database---
	public long insertTitle(String note, String imagelink, String tag,
			String address, String startdate, String enddate) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOTE, note);
		initialValues.put(KEY_IMAGELINK, imagelink);
		initialValues.put(KEY_TAG, tag);
		initialValues.put(KEY_ADDRESS, address);
		initialValues.put(KEY_STARTDATE, startdate);
		initialValues.put(KEY_ENDDATE, enddate);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	// ---deletes a particular row---
	public boolean deleteTitle(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	// ---retrieves all the diarytb---
	public Cursor getAllTitles() {
		return db.query(DATABASE_TABLE,
				new String[] { KEY_ROWID, KEY_NOTE, KEY_IMAGELINK, KEY_TAG,
						KEY_ADDRESS, KEY_STARTDATE, KEY_ENDDATE }, null, null,
				null, null, null);
	}

	// ---retrieves a particular row---
	public Cursor getTitle(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_NOTE, KEY_IMAGELINK, KEY_TAG, KEY_ADDRESS,
				KEY_STARTDATE, KEY_ENDDATE }, KEY_ROWID + "=" + rowId, null,
				null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---updates a row---
	public boolean updateTitle(long rowId, String note, String imagelink,
			String tag, String address, String startdate, String enddate) {
		ContentValues args = new ContentValues();
		args.put(KEY_NOTE, note);
		args.put(KEY_IMAGELINK, imagelink);
		args.put(KEY_TAG, tag);
		args.put(KEY_ADDRESS, address);
		args.put(KEY_STARTDATE, startdate);
		args.put(KEY_ENDDATE, enddate);
		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
}