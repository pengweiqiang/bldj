package com.bldj.lexiang.help;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.bldj.lexiang.utils.ImageTools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageDBAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_ALBUMS = "albums";
	public static final String KEY_PATH = "path";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_FILESIZE = "filesize";
	public static final String KEY_DURATION = "duration";
	private static final String TAG = "images";
	private static final String DATABASE_NAME = "thumbimages";
	private static final String DATABASE_TABLE_THUMBNAILS = "thumbnails";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE_TABLE_THUMBNAILS = "create table thumbnails (_id integer primary key autoincrement, "
			+ "name text, albums text, " + "path text, image blob);";
	
	private final Context context;
	
	private DatabaseHelper DBHelper;
	
	private SQLiteDatabase db;
	
	public ImageDBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "DatabaseHelper onCreated!");
			db.execSQL(DATABASE_CREATE_TABLE_THUMBNAILS);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS thumbnails");
			onCreate(db);
		}
	}
	
	/*****************************************************
	 * Below are all DBAdaptor method: create, open...
	 ****************************************************/
	
	public ImageDBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		DBHelper.close();
	}
	
	public long insertImage(String name, String albums, String path, Bitmap image) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_ALBUMS, albums);
		initialValues.put(KEY_PATH, path);
		// initialValues.put(KEY_IMAGE, getBitmap(image));
		long rowId = db.insert(DATABASE_TABLE_THUMBNAILS, null, initialValues);
		
		if (ImageTools.saveBitmap(albums + "_" + name, image)) {
			Log.i(TAG, "haha~~~~, save Image success!");
		} else
			Log.i(TAG, "nonono~~~~, save Image had wrong!");
		return rowId;
	}
	
	public boolean deleteImage(long rowId) {
		return db.delete(DATABASE_TABLE_THUMBNAILS, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public void deleteAllImage() {
		db.execSQL("DROP TABLE IF EXISTS thumbnails");
		db.execSQL(DATABASE_CREATE_TABLE_THUMBNAILS);
	}
	
	public Cursor getAllImages() {
		return db.query(DATABASE_TABLE_THUMBNAILS, new String[] { KEY_ROWID, KEY_NAME, KEY_ALBUMS, KEY_PATH }, null,
				null, null, null, null);
	}
	
	public Bitmap getImageById(int id) throws SQLException {
		Log.i(TAG, "getImageById running! the id=" + id);
		Cursor mCursor = db.query(true, DATABASE_TABLE_THUMBNAILS, new String[] { KEY_IMAGE }, KEY_ROWID + "=" + id,
				null, null, null, null, null);
		
		if (mCursor.moveToFirst()) {
			try {
				ByteArrayInputStream stream = new ByteArrayInputStream(mCursor.getBlob(0));
				mCursor.close();
				return BitmapFactory.decodeStream(stream);
			} catch (Exception err) {
				mCursor.close();
				err.printStackTrace();
				Log.i(TAG, "when ByteArrayInputStream , it's wrong!");
			}
		}
		return null;
	}
	
	public Cursor getImageByAlbum(String albums) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE_THUMBNAILS, new String[] { KEY_NAME, KEY_ROWID, KEY_PATH,
				KEY_IMAGE }, KEY_ALBUMS + "='" + albums + "'", null, null, null, null, null);
		return mCursor;
	}
	
	public boolean updateTitle(long rowId, String name, String albums, String path, Bitmap icon) {
		ContentValues args = new ContentValues();
		args.put(KEY_NAME, name);
		args.put(KEY_ALBUMS, albums);
		args.put(KEY_PATH, path);
		return db.update(DATABASE_TABLE_THUMBNAILS, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public byte[] getBitmap(Bitmap icon) {
		if (icon == null) {
			return null;
		}
		Log.i(TAG, "byte[] getBitmap runing!");
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		icon.compress(Bitmap.CompressFormat.PNG, 100, os);
		return os.toByteArray();
	}
	
}