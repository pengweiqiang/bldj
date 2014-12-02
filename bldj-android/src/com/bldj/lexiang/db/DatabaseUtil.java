package com.bldj.lexiang.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.utils.StringUtils;




public class DatabaseUtil {
	 private static final String TAG="DatabaseUtil";

	    private static DatabaseUtil instance;

	    /** 数据库帮助类 **/
	    private DBHelper dbHelper;

	    public synchronized static DatabaseUtil getInstance(Context context) {
	        if(instance == null) {
	            instance=new DatabaseUtil(context);
	        }
	        return instance;
	    }

	    /**
	     * 初始化
	     * @param context
	     */
	    private DatabaseUtil(Context context) {
	        dbHelper=new DBHelper(context);
	    }

	    /**
	     * 销毁
	     */
	    public static void destory() {
	        if(instance != null) {
	            instance.onDestory();
	        }
	    }

	    /**
	     * 销毁
	     */
	    public void onDestory() {
	        instance=null;
	        if(dbHelper != null) {
	            dbHelper.close();
	            dbHelper=null;
	        }
	    }
	    
	    /**
	     * 删除收藏
	     * @param qy
	     */
//	    public void delete( qy){
//	    	Cursor cursor=null;
//	    	String where = FavTable.USER_ID+" = '"+MyApplication.getInstance().getCurrentUser().getObjectId()
//	    			+"' AND "+FavTable.OBJECT_ID+" = '"+qy.getObjectId()+"'";
//	    	cursor=dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
//	    	if(cursor != null && cursor.getCount() > 0) {
//            	cursor.moveToFirst();
//            	int isLove = cursor.getInt(cursor.getColumnIndex(FavTable.IS_LOVE));
//            	if(isLove==0){
//            		dbHelper.delete(DBHelper.TABLE_NAME, where, null);
//            	}else{
//            		ContentValues cv = new ContentValues();
//            		cv.put(FavTable.IS_FAV, 0);
//            		dbHelper.update(DBHelper.TABLE_NAME, cv, where, null);
//            	}
//	    	}
//	    	if(cursor != null) {
//	            cursor.close();
//	            dbHelper.close();
//	        }
//	    }
//	    
//	    
//	    
//	    /**
//	     * 添加收藏
//	     * @param qy
//	     * @return
//	     */
//	    public long insertFav(QiangYu qy){
//	    	long uri = 0;
//	    	Cursor cursor=null;
//	    	String where = FavTable.USER_ID+" = '"+MyApplication.getInstance().getCurrentUser().getObjectId()
//	    			+"' AND "+FavTable.OBJECT_ID+" = '"+qy.getObjectId()+"'";
//            cursor=dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
//            if(cursor != null && cursor.getCount() > 0) {
//            	cursor.moveToFirst();
//            	ContentValues conv = new ContentValues();
//            	conv.put(FavTable.IS_FAV, 1);
//            	conv.put(FavTable.IS_LOVE, 1);
//            	dbHelper.update(DBHelper.TABLE_NAME, conv, where, null);
//            }else{
//		    	ContentValues cv = new ContentValues();
//		    	cv.put(FavTable.USER_ID, MyApplication.getInstance().getCurrentUser().getObjectId());
//		    	cv.put(FavTable.OBJECT_ID, qy.getObjectId());
//		    	cv.put(FavTable.IS_LOVE, qy.getMyLove()==true?1:0);
//		    	cv.put(FavTable.IS_FAV,qy.getMyFav()==true?1:0);
//		    	uri = dbHelper.insert(DBHelper.TABLE_NAME, null, cv);
//            }
//	    	 if(cursor != null) {
//		            cursor.close();
//		            dbHelper.close();
//		        }
//	    	return uri;
//	    }
//	    public long insertQiangyu(QiangYu qy){
//	    	long uri = 0;
//	    	Cursor cursor=null;
//	    	String where = QiangTable.OBJECT_ID+" = '"+qy.getObjectId()+"'";
//            cursor=dbHelper.query(DBHelper.TABLE_NAME_QIANGYU, null, where, null, null, null, null);
//            if(cursor != null && cursor.getCount() > 0) {
//            	cursor.moveToFirst();
//            	ContentValues conv = new ContentValues();
//            	conv.put(QiangTable.HATE, qy.getHate());
//            	conv.put(QiangTable.LOVE, qy.getLove());
//            	conv.put(QiangTable.COMMENT, qy.getComment());
//            	conv.put(QiangTable.MYFAV,qy.getMyFav()==true?1:0);
//            	
//            	dbHelper.update(DBHelper.TABLE_NAME_QIANGYU, conv, where, null);
//            }else{
//		    	ContentValues cv = new ContentValues();
//		    	cv.put(QiangTable.OBJECT_ID, qy.getObjectId());
//		    	cv.put(QiangTable.CONTENT, qy.getContent());
//		    	cv.put(QiangTable.HATE, qy.getHate());
//		    	cv.put(QiangTable.LOVE, qy.getLove());
//		    	
//		    	cv.put(QiangTable.COMMENT, qy.getComment());
//		    	cv.put(QiangTable.AUTHOROBJECTID, qy.getAuthorObjectId());
//		    	if(qy.getContentfigureurl()!=null){
//		    		cv.put(QiangTable.CONTENTURL, qy.getContentfigureurl().getFileUrl());
//		    	}
//		    	cv.put(QiangTable.USERNAME, qy.getAuthor().getUsername());
//		    	
//		    	cv.put(QiangTable.MYFAV,qy.getMyFav()==true?1:0);
//		    	String url = "";
//		    	if(qy.getAuthor().getAvatar()!=null){
//		    		url = qy.getAuthor().getAvatar().getFileUrl();
//		    	}else if(!StringUtils.isEmpty(qy.getAuthor().getFromType())){
//		    		url = qy.getAuthor().getFromType();
//		    	}
//		    	cv.put(QiangTable.USERURL,url);
//		    	
//		    	uri = dbHelper.insert(DBHelper.TABLE_NAME_QIANGYU, null, cv);
//            }
//	    	 if(cursor != null) {
//		            cursor.close();
//		            dbHelper.close();
//		        }
//	    	return uri;
//	    }
//	    
////	    public int deleteFav(QiangYu qy){
////	    	int row = 0;
////	    	String where = FavTable.USER_ID+" = "+qy.getAuthor().getObjectId()
////	    			+" AND "+FavTable.OBJECT_ID+" = "+qy.getObjectId();
////	    	row = dbHelper.delete(DBHelper.TABLE_NAME, where, null);
////	    	return row;
////	    }
//	    
//	    public void deteleCache(long id ){
//	    	String where = QiangTable._ID+" < "+id;
////	    			+" AND "+FavTable.OBJECT_ID+" = "+qy.getObjectId();
//	    	int row = dbHelper.delete(DBHelper.TABLE_NAME_QIANGYU, where, null);
//	    	System.out.println("row:::::::::::"+row);
//	    }
//	    
//	    
//	    /**
//	     * 设置内容的收藏状态
//	     * @param context
//	     * @param lists
//	     */
//	    public List<QiangYu> setFav(List<QiangYu> lists) {
//	        Cursor cursor=null;
//	        if(lists != null && lists.size() > 0) {
//	            for(Iterator iterator=lists.iterator(); iterator.hasNext();) {
//	            	QiangYu content=(QiangYu)iterator.next();
//	            	String where = FavTable.USER_ID+" = '"+MyApplication.getInstance().getCurrentUser().getObjectId()//content.getAuthor().getObjectId()
//	    	    			+"' AND "+FavTable.OBJECT_ID+" = '"+content.getObjectId()+"'";
//	                cursor=dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
//	                if(cursor != null && cursor.getCount() > 0) {
//	                	cursor.moveToFirst();
//	                	if(cursor.getInt(cursor.getColumnIndex(FavTable.IS_FAV))==1){
//	                		content.setMyFav(true);
//	                	}else{
//	                		content.setMyFav(false);
//	                	}
//	                    if(cursor.getInt(cursor.getColumnIndex(FavTable.IS_LOVE))==1){
//	                    	content.setMyLove(true);
//	                    }else{
//	                    	content.setMyLove(false);
//	                    }
//	                }
//	                LogUtils.i(TAG,content.getMyFav()+".."+content.getMyLove());
//	            }
//	        }
//	        if(cursor != null) {
//	            cursor.close();
//	            dbHelper.close();
//	        }
//	        return lists;
//	    }
//	    
//	    
//	    public ArrayList<QiangYu> queryFav() {
//	        ArrayList<QiangYu> contents=null;
//	        // ContentResolver resolver = context.getContentResolver();
//	        Cursor cursor=dbHelper.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
//	        LogUtils.i(TAG, cursor.getCount() + "");
//	        if(cursor == null) {
//	            return null;
//	        }
//	        contents=new ArrayList<QiangYu>();
//	        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//	        	QiangYu content=new QiangYu();
//	        	content.setMyFav(cursor.getInt(3)==1?true:false);
//	        	content.setMyLove(cursor.getInt(4)==1?true:false);
//	            LogUtils.i(TAG,cursor.getColumnIndex("isfav")+".."+cursor.getColumnIndex("islove")+".."+content.getMyFav()+"..."+content.getMyLove());
//	            contents.add(content);
//	        }
//	        if(cursor != null) {
//	            cursor.close();
//	        }
//	        // if (contents.size() > 0) {
//	        // return contents;
//	        // }
//	        return contents;
//	    }
//	    
//	    public ArrayList<QiangYu> queryQiangyuCache() {
//	        ArrayList<QiangYu> contents=null;
//	        String where = "1=1 order by _id Limit "+String.valueOf(40)+ " Offset " +String.valueOf(0); 
//	        // ContentResolver resolver = context.getContentResolver();
//	        Cursor cursor=dbHelper.query(DBHelper.TABLE_NAME_QIANGYU, null, where, null, null, null, null);
//	        LogUtils.i(TAG, cursor.getCount() +" size");
//	        if(cursor == null) {
//	            return null;
//	        }
//	        contents=new ArrayList<QiangYu>();
//	        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//	        	QiangYu content=new QiangYu();
//	        	content.setObjectId(cursor.getString(1));
//	        	content.setContent(cursor.getString(2));
//	        	content.setHate(cursor.getInt(3));
//	        	content.setLove(cursor.getInt(4));
//	        	content.setShare(cursor.getInt(5));
//	        	content.setComment(cursor.getInt(6));
//	        	content.setAuthorObjectId(cursor.getString(7));
//	        	content.setCacheUrl(cursor.getString(8));
//	        	
//	        	User author = new User();
//	        	author.setUsername(cursor.getString(9));
//	        	author.setFromType(cursor.getString(10));
//	        	
//	        	content.setAuthor(author);
//	        	content.setMyFav(cursor.getInt(11)==1?true:false);
//	        	
//	            LogUtils.i(TAG,cursor.getColumnIndex("isfav")+".."+cursor.getColumnIndex("islove")+".."+content.getMyFav()+"..."+content.getMyLove());
//	            contents.add(content);
//	        }
//	        if(cursor != null) {
//	            cursor.close();
//	        }
//	        // if (contents.size() > 0) {
//	        // return contents;
//	        // }
//	        return contents;
//	    }

}
