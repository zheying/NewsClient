package net.ym.zzy.favorite.data.tools;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by zengzheying on 15/3/27.
 */
public class CacheManager {

    private static int CACHE_TIME = 60 * 60000;

    public static void setCacheTime(int cacheTime){
        CACHE_TIME = cacheTime;
    }

    /**
     * 缓存至本地文件
     * @param context
     * @param ser
     * @param file
     * @return
     */
    public static boolean writeCacheObject(Context context, Serializable ser, String file){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try{
            fos = context.openFileOutput(file, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }finally {
            try{
                oos.close();
            }catch (Exception e){}
            try {
                fos.close();
            }catch (Exception e){}
        }
    }

    /**
     * 读取缓存对象
     * @param context
     * @param file
     * @return
     */
    public static Serializable readCacheObject(Context context, String file){
        if (!isExistDataCache(context, file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try{
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable)ois.readObject();
        }catch (FileNotFoundException e){

        }catch (Exception e){
            e.printStackTrace();
            //反序列化失败 - 删除缓存文件
            if ( e instanceof InvalidClassException){
                File data = context.getFileStreamPath(file);
                data.delete();
            }
        }finally {
            try{
                ois.close();
            }catch (Exception e){}
            try{
                fis.close();
            }catch (Exception e){}
        }
        return null;
    }

    /**
     * 判断缓存是否存在
     * @param context
     * @param cacheFile
     * @return
     */
    public static boolean isExistDataCache(Context context, String cacheFile){
        boolean exist = false;
        File data = context.getFileStreamPath(cacheFile);
        if (data.exists()){
            exist = true;
        }
        return exist;
    }

    /**
     * 判断缓存数据是否可读
     * @param context
     * @param cacheFile
     * @return
     */
    public static boolean isReadableDataCache(Context context, String cacheFile){
        return readCacheObject(context, cacheFile) != null;
    }

    /**
     * 判断缓存是否失效
     * @param context
     * @param cacheFile
     * @return
     */
    public static boolean isCacheDataFailure(Context context, String cacheFile){
        boolean failure = false;
        File data = context.getFileStreamPath(cacheFile);
        if (data.exists() && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
            failure = true;
        else if (!data.exists())
            failure = true;
        return failure;
    }

    /**
     * 判断方式是否兼容当前版本
     * @param vesionCode
     * @return
     */
    public static boolean isMethodsCompat(int vesionCode){
        int currentVersion = Build.VERSION.SDK_INT;
        return currentVersion >= vesionCode;
    }

    /**
     * 清除应用缓存
     * @param context
     */
    public static void clearCache(Context context){
        clearCacheFolder(context.getFilesDir(), System.currentTimeMillis());
        clearCacheFolder(context.getCacheDir(), System.currentTimeMillis());
        //2.2版本才有将应用缓存到sd卡的功能
        if (isMethodsCompat(Build.VERSION_CODES.FROYO)){
            clearCacheFolder(MethodsCompat.getExternalCacheDir(context), System.currentTimeMillis());
        }
    }

    /**
     * 清除缓存目录
     * @param dir 目录
     * @param curTime 当前系统时间
     * @return
     */
    public static int clearCacheFolder(File dir, long curTime){
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()){
            try{
                for (File child: dir.listFiles()){
                    if (child.isDirectory()){
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime){
                        if (child.delete()){
                            deletedFiles++;
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

}
