package io.dwak.holohackernews.app.cache;

import android.content.Context;
import android.support.v4.util.LruCache;

import com.google.gson.Gson;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.dwak.holohackernews.app.BuildConfig;

public class CacheManager {
    private static final int DEFAULT_CACHE_SIZE = 1024 * 1024 * 10;
    private static final int DEFAULT_MEM_CACHE_SIZE = 20;
    private static final int VALUE_COUNT = 1;
    private static final String HASH_ALGORITHM = "MD5";
    private static final String STRING_ENCODING = "UTF-8";
    private DiskLruCache mDiskLruCache;
    private final LruCache mMemCache;
    private final File mDirectory;
    private Gson mGson;
    private static CacheManager sCacheManager;

    public static CacheManager getInstance(Context context, Gson gson){
        if(sCacheManager == null){
            sCacheManager = new CacheManager(context, gson);
        }

        return sCacheManager;
    }
    public CacheManager() {
        mDiskLruCache = null;
        mMemCache = null;
        mDirectory = null;
        mGson = null;
    }

    public CacheManager(Context context, Gson gson){
        mDirectory= new File(context.getCacheDir() + "/cache");
        if (!mDirectory.exists()) {
            mDirectory.mkdirs();
        }
        mMemCache = new LruCache(DEFAULT_MEM_CACHE_SIZE);
        mGson = gson;
        open();
    }

    private void open() {
        try {
            mDiskLruCache = DiskLruCache.open(mDirectory, BuildConfig.VERSION_CODE, VALUE_COUNT, DEFAULT_CACHE_SIZE);
        } catch (IOException e) {
            Thread.dumpStack();
        }
    }


    public void clear() throws IOException {
        mDiskLruCache.delete();
        mMemCache.evictAll();
        open();
    }

    public boolean delete(String key) throws IOException {
        mMemCache.remove(key);
        return mDiskLruCache.remove(getHashOf(key));
    }

    public <T> T get(String key, Type type) throws IOException {
        T ob = (T) mMemCache.get(key);
        if (ob != null) {
            return ob;
        }
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(getHashOf(key));
        if (snapshot != null) {
            String value = snapshot.getString(0);
            ob = mGson.fromJson(value, type);
        }
        return ob;
    }

    public void put(String key, Object object) throws IOException {
        mMemCache.put(key, object);
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskLruCache.edit(getHashOf(key));
            if (editor == null) {
                return;
            }
            if (writeValueToCache(mGson.toJson(object), editor)) {
                mDiskLruCache.flush();
                editor.commit();
            }
            else {
                editor.abort();
            }
        } catch (IOException e) {
            if (editor != null) {
                editor.abort();
            }

            throw e;
        }

    }

    protected boolean writeValueToCache(String value, DiskLruCache.Editor editor) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(editor.newOutputStream(0));
            outputStream.write(value.getBytes(STRING_ENCODING));
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }

        return true;
    }


    protected String getHashOf(String string) throws UnsupportedEncodingException {
        try {
            MessageDigest messageDigest = null;
            messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            messageDigest.update(string.getBytes(STRING_ENCODING));
            byte[] digest = messageDigest.digest();
            BigInteger bigInt = new BigInteger(1, digest);

            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {

            return string;
        }
    }

}
