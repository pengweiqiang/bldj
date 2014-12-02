package com.bldj.lexiang.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	
	public static final String DATA_BASE_NAME = "bldj_fav_db";
	public static final int DATA_BASE_VERSION = 1;
	public static final String TABLE_NAME_PRODUCT = "fav_product";//经络养生
	public static final String TABLE_NAME_SELLER = "fav_seller";//美容师

	private SQLiteDatabase mDb;
	
	public DBHelper(Context context) {
		super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		onCreateProductTable(db);
		onCreateSellerTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	interface ProductTable{
		String _ID = "_id";
		String NAME = "name";
		String PICURL = "picurl";
		String CURPRICE = "curPrice";
		String MARKETPRICE = "marketPrice";
		String ONEWORD = "oneword";
		String TIMECONSUME = "timeConsume";
		String SELLERNUM = "sellerNum";
		String PRODETAILURL = "proDetailUrl";
		String SUITSCROWD = "suitsCrowd";
				
	}
	interface SellerTable{
		String _ID = "_id";
		String USERNAME = "username";
		String NICKNAME = "nickname";
		String AREA = "area";
		String RECOMMEND = "recommend";
		String MOBILE = "mobile";
		String ADDRESS = "address";
		String AVGPRICE = "avgPrice";
		String USERGRADE = "userGrade";
		String HEADURL = "headurl";
		String WORKYEAR = "workyear";
		String DEALNUMSUM = "dealnumSum";
		String DISTANCE = "distance";
		
	}
	/**
	 * 产品收藏
	 * @param db
	 */
	private void onCreateProductTable(SQLiteDatabase db){
		  StringBuilder favStr=new StringBuilder();
	      favStr.append("CREATE TABLE IF NOT EXISTS ")
	      		.append(DBHelper.TABLE_NAME_PRODUCT)
	      		.append(" ( ").append(ProductTable._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
	      		.append(ProductTable.NAME).append(" varchar(100),")
	      		.append(ProductTable.PICURL).append(" varchar(200),")
	      		.append(ProductTable.CURPRICE).append(" double,")
	      		.append(ProductTable.MARKETPRICE).append(" double,")
	      		.append(ProductTable.ONEWORD).append(" varchar(100),")
	      		.append(ProductTable.TIMECONSUME).append(" Integer,")
	      		.append(ProductTable.SELLERNUM).append(" Integer,")
	      		.append(ProductTable.PRODETAILURL).append(" varchar(200),")
	      		.append(ProductTable.SUITSCROWD).append(" varchar(100);");
	      db.execSQL(favStr.toString());
	}
	
	/**
	 * 美容师收藏
	 * @param db
	 */
	private void onCreateSellerTable(SQLiteDatabase db){
		  StringBuilder favStr=new StringBuilder();
	      favStr.append("CREATE TABLE IF NOT EXISTS ")
	      		.append(DBHelper.TABLE_NAME_SELLER)
	      		.append(" ( ").append(SellerTable._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
	      		.append(SellerTable.USERNAME).append(" varchar(100),")
	      		.append(SellerTable.NICKNAME).append(" varchar(100),")
	      		.append(SellerTable.AREA).append(" text,")
	      		.append(SellerTable.RECOMMEND).append(" text,")
	      		.append(SellerTable.MOBILE).append(" varchat(50),")
	      		.append(SellerTable.ADDRESS).append(" text,")
	      		.append(SellerTable.AVGPRICE).append(" varchar(50),")
	      		.append(SellerTable.USERGRADE).append(" text,")
	      		.append(SellerTable.HEADURL).append(" varchar(200),")
	      		.append(SellerTable.WORKYEAR).append(" integer,")
	      		.append(SellerTable.DEALNUMSUM).append(" integer,")
	      		.append(SellerTable.DISTANCE).append(" double);");
	      db.execSQL(favStr.toString());
	}
	
	
    /**
     * 获取数据库操作对象
     * @param isWrite 是否可写
     * @return
     */
    public synchronized SQLiteDatabase getDatabase(boolean isWrite) {

        if(mDb == null || !mDb.isOpen()) {
            if(isWrite) {
                try {
                    mDb=getWritableDatabase();
                } catch(Exception e) {
                    // 当数据库不可写时
                    mDb=getReadableDatabase();
                    return mDb;
                }
            } else {
                mDb=getReadableDatabase();
            }
        }
        // } catch (SQLiteException e) {
        // // 当数据库不可写时
        // mDb = getReadableDatabase();
        // }
        return mDb;
    }
    
    public int delete(String table, String whereClause, String[] whereArgs) {
        getDatabase(true);
        return mDb.delete(table, whereClause, whereArgs);
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        getDatabase(true);
        return mDb.insertOrThrow(table, nullColumnHack, values);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        getDatabase(true);
        return mDb.update(table, values, whereClause, whereArgs);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        getDatabase(false);
        return mDb.rawQuery(sql, selectionArgs);
    }

    public void execSQL(String sql) {
        getDatabase(true);
        mDb.execSQL(sql);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
    // final
        String orderBy) {
        getDatabase(false);
        return mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

}
