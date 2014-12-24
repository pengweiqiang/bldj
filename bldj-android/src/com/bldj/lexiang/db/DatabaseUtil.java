package com.bldj.lexiang.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.bldj.lexiang.api.vo.Ad;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.db.DBHelper.BannerTable;
import com.bldj.lexiang.db.DBHelper.ProductTable;
import com.bldj.lexiang.db.DBHelper.SellerTable;

public class DatabaseUtil {
	private static final String TAG = "DatabaseUtil";

	private static DatabaseUtil instance;

	/** 数据库帮助类 **/
	private DBHelper dbHelper;

	public synchronized static DatabaseUtil getInstance(Context context) {
		if (instance == null) {
			instance = new DatabaseUtil(context);
		}
		return instance;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	private DatabaseUtil(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 销毁
	 */
	public static void destory() {
		if (instance != null) {
			instance.onDestory();
		}
	}

	/**
	 * 销毁
	 */
	public void onDestory() {
		instance = null;
		if (dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		}
	}

	/**
	 * 删除收藏
	 * 
	 * @param qy
	 */
	// public void delete( qy){
	// Cursor cursor=null;
	// String where =
	// FavTable.USER_ID+" = '"+MyApplication.getInstance().getCurrentUser().getObjectId()
	// +"' AND "+FavTable.OBJECT_ID+" = '"+qy.getObjectId()+"'";
	// cursor=dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null,
	// null);
	// if(cursor != null && cursor.getCount() > 0) {
	// cursor.moveToFirst();
	// int isLove = cursor.getInt(cursor.getColumnIndex(FavTable.IS_LOVE));
	// if(isLove==0){
	// dbHelper.delete(DBHelper.TABLE_NAME, where, null);
	// }else{
	// ContentValues cv = new ContentValues();
	// cv.put(FavTable.IS_FAV, 0);
	// dbHelper.update(DBHelper.TABLE_NAME, cv, where, null);
	// }
	// }
	// if(cursor != null) {
	// cursor.close();
	// dbHelper.close();
	// }
	// }
	//
	//
	//
	/**
	 * 添加收藏产品
	 * 
	 * @param product
	 * @return
	 */
	public long insertProduct(Product product,int type) {
		long uri = 0;
		Cursor cursor = null;
		String where = ProductTable.PRODUCTID + " = " + product.getId() +" and "+ProductTable.TYPE +" = "+type;
		cursor = dbHelper.query(DBHelper.TABLE_NAME_PRODUCT, null, where, null,
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			return 1;
		} else {
			ContentValues cv = new ContentValues();
			cv.put(ProductTable.PRODUCTID, product.getId());
			cv.put(ProductTable.NAME, product.getName());
			cv.put(ProductTable.PICURL, product.getPicurl());
			cv.put(ProductTable.CURPRICE, product.getCurPrice());
			cv.put(ProductTable.MARKETPRICE, product.getMarketPrice());
			cv.put(ProductTable.ONEWORD, product.getOneword());
			cv.put(ProductTable.TIMECONSUME, product.getTimeConsume());
			cv.put(ProductTable.SELLERNUM, product.getSellerNum());
			cv.put(ProductTable.PRODETAILURL, product.getProDetailUrl());
			cv.put(ProductTable.SUITSCROWD, product.getSuitsCrowd());
			cv.put(ProductTable.TYPE, type);
			uri = dbHelper.insert(DBHelper.TABLE_NAME_PRODUCT, null, cv);
		}
		if (cursor != null) {
			cursor.close();
			dbHelper.close();
		}
		return uri;
	}

	/**
	 * 收藏添加美容师
	 * 
	 * @param qy
	 * @return
	 */
	public long insertSeller(Seller seller) {
		long uri = 0;
		Cursor cursor = null;
		String where = SellerTable.SELLERID + " = " + seller.getId();
		cursor = dbHelper.query(DBHelper.TABLE_NAME_SELLER, null, where, null,
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			/*
			 * cursor.moveToFirst(); ContentValues conv = new ContentValues();
			 * conv.put(QiangTable.HATE, qy.getHate());
			 * conv.put(QiangTable.LOVE, qy.getLove());
			 * conv.put(QiangTable.COMMENT, qy.getComment());
			 * conv.put(QiangTable.MYFAV,qy.getMyFav()==true?1:0);
			 * 
			 * dbHelper.update(DBHelper.TABLE_NAME_QIANGYU, conv, where, null);
			 */
			return uri;
		} else {
			ContentValues cv = new ContentValues();
			cv.put(SellerTable.SELLERID, seller.getId());
			cv.put(SellerTable.USERNAME, seller.getUsername());
			cv.put(SellerTable.NICKNAME, seller.getNickname());
			cv.put(SellerTable.AREA, seller.getArea());
			cv.put(SellerTable.RECOMMEND, seller.getRecommend());
			cv.put(SellerTable.MOBILE, seller.getMobile());
			cv.put(SellerTable.ADDRESS, seller.getAddress());
			cv.put(SellerTable.AVGPRICE, seller.getAvgPrice());
			cv.put(SellerTable.USERGRADE, seller.getUserGrade());
			cv.put(SellerTable.HEADURL, seller.getHeadurl());
			cv.put(SellerTable.WORKYEAR, seller.getWorkyear());
			cv.put(SellerTable.DEALNUMSUM, seller.getDealnumSum());
			cv.put(SellerTable.DISTANCE, seller.getDistance());
			cv.put(SellerTable.DETAILURL, seller.getDetailUrl());

			uri = dbHelper.insert(DBHelper.TABLE_NAME_SELLER, null, cv);
		}
		if (cursor != null) {
			cursor.close();
			dbHelper.close();
		}
		return uri;
	}

	//
	/**
	 * 取消收藏产品
	 * 
	 * @param product
	 * @return
	 */
	public int deleteFavProduct(long productId,int type) {
		int row = 0;
		String where = ProductTable.PRODUCTID + " = " + productId+" and "+ProductTable.TYPE +" = "+type;
		row = dbHelper.delete(DBHelper.TABLE_NAME_PRODUCT, where, null);
		return row;
	}

	/**
	 * 取消收藏美容师
	 * 
	 * @param sellerId
	 * @return
	 */
	public int deleteFavSeller(long sellerId) {
		int row = 0;
		String where = SellerTable.SELLERID + " = " + sellerId;
		row = dbHelper.delete(DBHelper.TABLE_NAME_SELLER, where, null);
		return row;
	}

	//
	// public void deteleCache(long id ){
	// String where = QiangTable._ID+" < "+id;
	// // +" AND "+FavTable.OBJECT_ID+" = "+qy.getObjectId();
	// int row = dbHelper.delete(DBHelper.TABLE_NAME_QIANGYU, where, null);
	// System.out.println("row:::::::::::"+row);
	// }
	//
	//
	// /**
	// * 设置内容的收藏状态
	// * @param context
	// * @param lists
	// */
	// public List<QiangYu> setFav(List<QiangYu> lists) {
	// Cursor cursor=null;
	// if(lists != null && lists.size() > 0) {
	// for(Iterator iterator=lists.iterator(); iterator.hasNext();) {
	// QiangYu content=(QiangYu)iterator.next();
	// String where =
	// FavTable.USER_ID+" = '"+MyApplication.getInstance().getCurrentUser().getObjectId()//content.getAuthor().getObjectId()
	// +"' AND "+FavTable.OBJECT_ID+" = '"+content.getObjectId()+"'";
	// cursor=dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null,
	// null);
	// if(cursor != null && cursor.getCount() > 0) {
	// cursor.moveToFirst();
	// if(cursor.getInt(cursor.getColumnIndex(FavTable.IS_FAV))==1){
	// content.setMyFav(true);
	// }else{
	// content.setMyFav(false);
	// }
	// if(cursor.getInt(cursor.getColumnIndex(FavTable.IS_LOVE))==1){
	// content.setMyLove(true);
	// }else{
	// content.setMyLove(false);
	// }
	// }
	// LogUtils.i(TAG,content.getMyFav()+".."+content.getMyLove());
	// }
	// }
	// if(cursor != null) {
	// cursor.close();
	// dbHelper.close();
	// }
	// return lists;
	// }
	//
	//
	/**
	 * 检查是否收藏过该产品
	 * 
	 * @param productId
	 * @return
	 */
	public boolean checkFavProduct(long productId) {
		String where = ProductTable.PRODUCTID + " = " + productId +" and "+ ProductTable.TYPE +" = 0";
		Cursor cursor = dbHelper.query(DBHelper.TABLE_NAME_PRODUCT, null,
				where, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 检查是否收藏过该美容师
	 * 
	 * @param sellerId
	 * @return
	 */
	public boolean checkFavSeller(long sellerId) {
		String where = SellerTable.SELLERID + " = " + sellerId;
		Cursor cursor = dbHelper.query(DBHelper.TABLE_NAME_SELLER, null, where,
				null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 获取收藏的产品
	 * 
	 * @return
	 */
	public ArrayList<Product> queryFavProduct(int pageNubmer, int limit,int type) {
		ArrayList<Product> products = null;
		// ContentResolver resolver = context.getContentResolver();
		// String where = "1=1 order by _id Limit "+String.valueOf(40)+
		// " Offset " +String.valueOf(0);
		Cursor cursor = null;
		if(type == 0){//收藏数据
			int firstResult = pageNubmer * limit;
			int maxResult = (pageNubmer + 1) * limit;
			String where = ProductTable.TYPE +" = "+type+" order by _id limit " + firstResult + ","
					+ maxResult;
			cursor = dbHelper.query(DBHelper.TABLE_NAME_PRODUCT, null,
					where, null, null, null, null);
		}else if (type == 1){//缓存数据
			String where = ProductTable.TYPE +" = "+ type;
			cursor = dbHelper.query(DBHelper.TABLE_NAME_PRODUCT, null,
					where, null, null, null, null);
		}
		if (cursor == null) {
			return null;
		}
		products = new ArrayList<Product>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Product product = new Product();

			product.setId(cursor.getInt(1));
			product.setName(cursor.getString(2));
			product.setPicurl(cursor.getString(3));
			product.setCurPrice(cursor.getDouble(4));
			product.setMarketPrice(cursor.getDouble(5));
			product.setOneword(cursor.getString(6));
			product.setTimeConsume(cursor.getLong(7));
			product.setSellerNum(cursor.getInt(8));
			product.setProDetailUrl(cursor.getString(9));
			product.setSuitsCrowd(cursor.getString(10));

			products.add(product);
		}
		if (cursor != null) {
			cursor.close();
		}
		// if (contents.size() > 0) {
		// return contents;
		// }
		return products;
	}

	/**
	 * 获取收藏的美容师列表
	 * 
	 * @param pageNum
	 * @param limit
	 * @return
	 */
	public ArrayList<Seller> querySellers(int pageNubmer, int limit) {
		ArrayList<Seller> contents = null;

		// ContentResolver resolver = context.getContentResolver();
		int firstResult = pageNubmer * limit;
		int maxResult = (pageNubmer + 1) * limit;
		// String where = "1=1 order by _id Limit "+String.valueOf(40)+
		// " Offset " +String.valueOf(0);
		String where = "1=1 order by _id limit " + firstResult + ","
				+ maxResult;
		Cursor cursor = dbHelper.query(DBHelper.TABLE_NAME_SELLER, null, where,
				null, null, null, null);
		if (cursor == null) {
			return null;
		}
		contents = new ArrayList<Seller>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Seller content = new Seller();
			content.setId(cursor.getInt(1));
			content.setUsername(cursor.getString(2));
			content.setNickname(cursor.getString(3));
			content.setArea(cursor.getString(4));
			content.setRecommend(cursor.getString(5));
			content.setMobile(cursor.getString(6));
			content.setAddress(cursor.getString(7));
			content.setAvgPrice(cursor.getDouble(8));
			content.setUserGrade(cursor.getInt(9));
			content.setHeadurl(cursor.getString(10));
			content.setWorkyear(cursor.getInt(11));
			content.setDealnumSum(cursor.getInt(12));
			content.setDistance(cursor.getDouble(13));
			content.setDetailUrl(cursor.getString(14));

			contents.add(content);
		}
		if (cursor != null) {
			cursor.close();
		}
		// if (contents.size() > 0) {
		// return contents;
		// }
		return contents;
	}

	/**
	 * 缓存首页产品数据
	 * 
	 * @param products
	 */
	public void insertProductsList(List<Product> products,int type) {
		Cursor cursor = null;
		if (products != null && !products.isEmpty()) {
			dbHelper.delete(DBHelper.TABLE_NAME_PRODUCT, null, null);
			for (Product product : products) {

//				String where = ProductTable.PRODUCTID + " = " + product.getId();
//				cursor = dbHelper.query(DBHelper.TABLE_NAME_PRODUCT, null,
//						where, null, null, null, null);
//				if (cursor != null && cursor.getCount() > 0) {
//					cursor.moveToFirst();
//					// ContentValues conv = new ContentValues();
//					//
//					// dbHelper.update(DBHelper.TABLE_NAME_QIANGYU, conv, where,
//					// null);
//				} else {
					ContentValues cv = new ContentValues();
					cv.put(ProductTable.PRODUCTID, product.getId());
					cv.put(ProductTable.NAME, product.getName());
					cv.put(ProductTable.PICURL, product.getPicurl());
					cv.put(ProductTable.CURPRICE, product.getCurPrice());
					cv.put(ProductTable.MARKETPRICE, product.getMarketPrice());
					cv.put(ProductTable.ONEWORD, product.getOneword());
					cv.put(ProductTable.TIMECONSUME, product.getTimeConsume());
					cv.put(ProductTable.SELLERNUM, product.getSellerNum());
					cv.put(ProductTable.PRODETAILURL, product.getProDetailUrl());
					cv.put(ProductTable.SUITSCROWD, product.getSuitsCrowd());
					cv.put(ProductTable.TYPE, type);
					long uri = dbHelper.insert(DBHelper.TABLE_NAME_PRODUCT,
							null, cv);
					System.out.println("缓存数据："+uri);

//				}

			}
		}
		if (cursor != null) {
			cursor.close();
			dbHelper.close();
		}

	}
	
	/**
	 * 缓存首页广告
	 * 
	 * @param products
	 */
	public void insertBannerList(List<Ad> ads) {
//		Cursor cursor = null;
		if (ads != null && !ads.isEmpty()) {
			dbHelper.delete(DBHelper.TABLE_NAME_BANNER, null, null);
			for (Ad ad : ads) {

//				String where = ProductTable.PRODUCTID + " = " + product.getId();
//				cursor = dbHelper.query(DBHelper.TABLE_NAME_PRODUCT, null,
//						where, null, null, null, null);
//				if (cursor != null && cursor.getCount() > 0) {
//					cursor.moveToFirst();
//					// ContentValues conv = new ContentValues();
//					//
//					// dbHelper.update(DBHelper.TABLE_NAME_QIANGYU, conv, where,
//					// null);
//				} else {
					ContentValues cv = new ContentValues();
					cv.put(BannerTable.NAME, ad.getName());
					cv.put(BannerTable.PICURL, ad.getPicurl());
					cv.put(BannerTable.LEAVETIME, ad.getLeavetime());
					cv.put(BannerTable.ACTIONURL, ad.getActionUrl());
					long uri = dbHelper.insert(DBHelper.TABLE_NAME_BANNER,
							null, cv);
					System.out.println("缓存数据："+uri);

//				}

			}
		}
//		if (cursor != null) {
//			cursor.close();
//			dbHelper.close();
//		}

	}
	
	
	/**
	 * 获取收藏的美容师列表
	 * 
	 * @param pageNum
	 * @param limit
	 * @return
	 */
	public ArrayList<Ad> queryAds() {
		ArrayList<Ad> contents = null;

		// ContentResolver resolver = context.getContentResolver();
		// String where = "1=1 order by _id Limit "+String.valueOf(40)+
		// " Offset " +String.valueOf(0);
		Cursor cursor = dbHelper.query(DBHelper.TABLE_NAME_SELLER, null, null,
				null, null, null, null);
		if (cursor == null) {
			return null;
		}
		contents = new ArrayList<Ad>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Ad content = new Ad();
			content.setName(cursor.getString(1));
			content.setPicurl(cursor.getString(2));
			content.setLeavetime(cursor.getString(3));
			content.setActionUrl(cursor.getString(4));

			contents.add(content);
		}
		if (cursor != null) {
			cursor.close();
		}
		// if (contents.size() > 0) {
		// return contents;
		// }
		return contents;
	}
	

	/**
	 * 获取产品明细
	 * 
	 * @param productId
	 * @return
	 */
	public Product getProductById(long productId) {
		Cursor cursor = null;
		Product product = null;
		String where = ProductTable.PRODUCTID + " = " + productId;
		cursor = dbHelper.query(DBHelper.TABLE_NAME_PRODUCT, null, where, null,
				null, null, null);
		if (cursor == null) {
			return null;
		}
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			product = new Product();

			product.setId(cursor.getInt(1));
			product.setName(cursor.getString(2));
			product.setPicurl(cursor.getString(3));
			product.setCurPrice(cursor.getDouble(4));
			product.setMarketPrice(cursor.getDouble(5));
			product.setOneword(cursor.getString(6));
			product.setTimeConsume(cursor.getLong(7));
			product.setSellerNum(cursor.getInt(8));
			product.setProDetailUrl(cursor.getString(9));
			product.setSuitsCrowd(cursor.getString(10));
		}
		
		if (cursor != null) {
			cursor.close();
		}
		return product;

	}

}
