package com.bldj.lexiang.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

import com.bldj.lexiang.GlobalConfig;
public class FileUtils {
	
	public static final String parentDirName = "renrenGames";
	
	public static final String downloadDirName = "download";
	
	public static final String cache_path = "/data/data/com.sitech.store/shared_prefs/";
	
	public static final int S_HAS_DB = 1;// 本地根目录下存在db
	
	public static final int S_HAS_NO_DB = 0;// 本地根目录下不存在db
	
	public static boolean isSDcardMounted() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
	
	public static List<String> getPathFiles(String path) {
		if (path == null || "".equals(path)) {
			return null;
		}
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {
			return null;
		}
		String[] children = file.list();
		return children == null ? new ArrayList<String>() : Arrays.asList(children);
	}
	
	/**
	 * 保存图片到SD卡
	 * 
	 * @param imagePath
	 * @param buffer
	 * @throws IOException
	 */
	public static boolean saveImage(String imagePath, String format, Bitmap bitmap) throws IOException {
		File f = new File(imagePath);
		if (f.exists()) {
			f.delete();
		}
		
		File parentFile = f.getParentFile();
		if (parentFile == null) {
			return false;
		}
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		f.createNewFile();
		FileOutputStream fos = new FileOutputStream(imagePath);
		
		CompressFormat cf = null;
		
		if (format.toLowerCase().contains("jpg") || format.toLowerCase().contains("jpeg")) {
			cf = CompressFormat.JPEG;
		} else {
			cf = CompressFormat.PNG;
		}
		
		boolean result = bitmap.compress(cf, 80, fos);
		if (result) {
			fos.flush();
			fos.close();
		} else {
			if (f.exists()) {
				f.delete();
			}
		}
		return true;
	}
	
	/**
	 * 删除缓存文件
	 * 
	 * @param path 文件路径
	 * @return 是否成功
	 */
	public static boolean deleteCache(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return true;
		} else {
			if (file.isFile()) {
				return deleteFile(path);
			} else {
				return deleteDirectory(path);
			}
		}
	}
	
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean deleteFile(File file) {
		if (file != null && file.isFile() && file.exists()) {
			file.delete();
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * 获取文件大小
	 */
	public static long getFileSize(File file) {
		if (file != null && file.isFile() && file.exists()) {
			long length = file.length();
			if (length < 10) {
				length = 0;
			} else {
				length -= 10;
			}
			return length;
		} else {
			return 0L;
		}
	}
	
	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param dir 被删除目录的文件路径
	 * @return 目录删除成功返回true,否则返回false
	 */
	public static boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
		}
		
		if (!flag) {
			return false;
		}
		
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 保存缓存数据到本地(对象流)
	 * 
	 * @param path 路径
	 * @param fileName 文件名
	 * @param data 保存内容
	 * @return
	 */
	public static boolean saveCacheData(String path, String fileName, Object data) {
		if (!GlobalConfig.isOpenCache) {
			return false;
		}
		boolean isSucceed = false;
		if (CommonHelper.isSDCardMounted()) {
			if (data != null) {
				File fileFolder = new File(path);
				if (!fileFolder.exists()) {
					fileFolder.mkdirs();
				}
				File file = new File(fileFolder, fileName);
				FileOutputStream fos = null;
				ObjectOutputStream oos = null;
				try {
					fos = new FileOutputStream(file);
					oos = new ObjectOutputStream(fos);
					oos.writeObject(data);
					isSucceed = true;
				} catch (Exception e) {
					file.delete();
					e.printStackTrace();
				} finally {
					if (oos != null) {
						try {
							oos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			
		}
		
		return isSucceed;
		
	}
	
	/**
	 * 读本地的对象流数据
	 */
	public static Object readCacheData(String path, String fileName) {
		if (!GlobalConfig.isOpenCache) {
			return null;
		}
		Object obj = null;
		if (CommonHelper.isSDCardMounted()) {
			File fileFolder = new File(path);
			if (!fileFolder.exists()) {
				return null;
			}
			
			File file = new File(fileFolder, fileName);
			
			if (!file.exists()) {
				return null;
			}
			
			FileInputStream fileInputStream = null;
			ObjectInputStream inputStream = null;
			try {
				fileInputStream = new FileInputStream(file);
				inputStream = new ObjectInputStream(fileInputStream);
				obj = inputStream.readObject();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (fileInputStream != null) {
					try {
						fileInputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
		
		return obj;
	}
	
	/**
	 * 保存缓存数据或者时间戳到本地
	 * 
	 * @param requestId
	 * @param value
	 */
	public static void saveData(String fileName, String value) {
		
		if (fileName != null && value != null) {
			FileUtils.saveCacheData(GlobalConfig.CACHE_DATA_PATH, fileName, value);
		}
	}
	
	/**
	 * 获得保存本地的路径
	 * 
	 * @param imgUrl
	 * @return
	 */
	public static String getSaveImgPath(Context context, String imgUrl) {
		if (CommonHelper.isSDCardMounted()) {
			String imagePath = GlobalConfig.CACHE_IMG_PATH + (StringUtils.md5(imgUrl));
			return imagePath;
		}
		return context.getCacheDir() + StringUtils.md5(imgUrl);
	}
	
	/**
	 * 获得本地图片缓存路径
	 * 
	 * @param imgUrl
	 * @return
	 */
	public static File getImgCacheDir(Context context) {
		if (CommonHelper.isSDCardMounted()) {
			String imagePath = GlobalConfig.CACHE_IMG_PATH;
			return new File(imagePath);
		}
		return context.getCacheDir();
	}
	
	/**
	 * 读字符流
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static String readSteam(InputStream inputStream) throws IOException {
		StringBuffer resultBuffer = new StringBuffer();
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
		String str = new String("");
		while ((str = bufferReader.readLine()) != null) {
			resultBuffer.append(str);
		}
		inputStream.close();
		bufferReader.close();
		return resultBuffer.toString();
	}
	
	/**
	 * 读字节流
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readByteSteam(InputStream inputStream) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = -1;
		while ((length = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, length);
		}
		inputStream.close();
		outStream.close();
		return outStream.toByteArray();
	}
	
	public static void delete(String filename) {// 删除文件
		/** 删除SharedPreferences文件 **/
		File file = new File(cache_path + filename + ".xml");
		if (file.exists()) {
			file.delete();
		}
	}
	
//	/**
//	 * 线上文件copy到本地
//	 * 
//	 * @param onlineFile
//	 * @param localFile
//	 * @return
//	 */
//	public static String copyOnline2Local(String onlineFile, String localFile) {
//		InputStream input = null;
//		FileOutputStream out = null;
//		StringBuffer buf = new StringBuffer();
//		try {
//			input = NetUtil.getStreamFromUrl(NetUtil.getConectionUrlWithoutPort(onlineFile, true));
//			File aFile = makeFile(localFile);
//			out = new FileOutputStream(aFile);
//			int len;
//			byte[] buffer = new byte[1024];
//			while ((len = input.read(buffer)) != -1) {
//				out.write(buffer, 0, len);
//				out.flush();
//				buf.append(new String(buffer, 0, len));
//			}
//			if (input != null) {
//				input.close();
//			}
//			if (out != null) {
//				out.close();
//			}
//		} catch (IOException e) {
//			Log.d("copyOnline2Local", "========copyOnline2Local exception========");
//			e.printStackTrace();
//		}
//		return buf.toString();
//	}
	
	public static int copy(String fromFile, String toFile) {
		// 要复制的文件目录
		File[] currentFiles;
		File root = new File(fromFile);
		// 如同判断SD卡是否存在或者文件是否存在
		// 如果不存在则 return出去
		if (!root.exists()) {
			return -1;
		}
		// 如果存在则获取当前目录下的全部文件 填充数组
		currentFiles = root.listFiles();
		
		// 目标目录
		File targetDir = new File(toFile);
		// 创建目录
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		// 遍历要复制该目录下的全部文件
		for (int i = 0; i < currentFiles.length; i++) {
			if (currentFiles[i].isDirectory())// 如果当前项为子目录 进行递归
			{
				copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");
				
			} else// 如果当前项为文件则进行文件拷贝
			{
				CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
			}
		}
		return 0;
	}
	
	// 文件拷贝
	// 要复制的目录下的所有非子目录(文件夹)文件拷贝
	public static int CopySdcardFile(String fromFile, String toFile) {
		
		try {
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return 0;
			
		} catch (Exception ex) {
			return -1;
		}
	}
	
	// 文件拷贝
	// 要复制的目录下的所有非子目录(文件夹)文件拷贝
	public static void CopySdcardFile(InputStream fosfrom, String toFile) {
		
		try {
			// InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			
		} catch (Exception ex) {
		}
	}

    /**
     * 读取文件内容
     *
     * @param fileName
     *            目标文件的名字
     * @return
     */
    public static String readFile(String fileName) {
        try {
            InputStream is = new FileInputStream(fileName);
            StringBuffer sBuffer = new StringBuffer();
            InputStreamReader inputStreamReader = new InputStreamReader(is,
                    Charset.forName("UTF-8"));
            BufferedReader in = new BufferedReader(inputStreamReader);
            while (in.ready()) {
                sBuffer.append(in.readLine());
            }
            in.close();
            return sBuffer.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存文件
     *
     * @param file
     *            目标文件
     * @param content
     *            要保存的内容
     * @throws IOException
     */
    public static void saveFile(String fileName,String content){
        try{
            FileOutputStream fout = new FileOutputStream(fileName);
            byte [] bytes = content.getBytes();
            fout.write(bytes);
            fout.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

	public static File makeFile(String path) throws IOException {
		File aFile = new File(path);
		if(aFile.exists()) return aFile;
		
			if (!aFile.getParentFile().mkdirs()) {
				
			}
		
		aFile.createNewFile();
		return aFile;
	}
	
	public static File makeFile(File aFile) throws IOException {
		if (!aFile.exists()) {
			if (!aFile.getParentFile().mkdirs()) {
				
			}
		}
		aFile.createNewFile();
		return aFile;
	}
	
	public static String getCachePath(Context context) {
		// String path =
		// context.getFilesDir().getPath()+"data"+context.getFilesDir().getPath()+"data"+context.getFilesDir().getPath()
		// + context.getPackageName() + context.getFilesDir().getPath()+"cache";
		String path = "";
		File file = context.getCacheDir();
		if (file != null) {
			path = file.getAbsolutePath();
		}
		return path;
	}
	
	public static File getCacheDir(Context context) {
		String path = getCachePath(context);
		File aDataDir = new File(path);
		if (!aDataDir.exists()) {
			aDataDir.mkdir();
		}
		return aDataDir;
	}
	
	/**
	 * 获取本地DB文件path
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocalDBFilePath(Context context, String fileName) {
		String aLocalFile = getCacheDir(context).getPath() + File.separator + fileName;
		return aLocalFile;
	}
	
	public static int check(String dir, String fileName) {
		File file = new File(dir, fileName);
		if (file.exists()) {
			return S_HAS_DB;
		}
		return S_HAS_NO_DB;
	}
	
	public static int check(String filePath) {
		if (StringUtils.isEmpty(filePath))
			return S_HAS_NO_DB;
		File file = new File(filePath);
		if (file.exists()) {
			return S_HAS_DB;
		}
		return S_HAS_NO_DB;
	}
}
