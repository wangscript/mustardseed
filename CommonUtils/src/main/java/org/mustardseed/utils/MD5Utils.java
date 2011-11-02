package org.mustardseed.utils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *MD5相关工具类
 *@author HermitWayne
 */
public class MD5Utils {
    private static char hexDigits[] 
	= {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    
    public static MessageDigest getMd5Digest() {
	MessageDigest ret = null;
	try {
	    ret = MessageDigest.getInstance("MD5");
	} catch (NoSuchAlgorithmException e) {}
	return ret;
    }
    /**
     *获得File对象的MD5
     */
    public static String hash(File file) {
	try {
	    FileInputStream in = new FileInputStream(file);
	    FileChannel ch = in.getChannel();
	    MappedByteBuffer byteBuffer 
		= ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
	    return hash(byteBuffer);
	} catch (Exception e) {
	    return e.toString();
	}
    }
    /**
     *获得ByteBuffer的MD5
     */
    public static String hash(ByteBuffer byteBuffer) {
	MessageDigest md5Digest = getMd5Digest();
	md5Digest.update(byteBuffer);
	return hexString(md5Digest.digest());
    }
    /**
     *获得byte数组的MD5
     */
    public static String hash(byte[] bytes) {
	return hash(ByteBuffer.wrap(bytes));
    }
    /**
     *获得字符串的MD5
     */
    public static String hash(String text) {
	return hash(text.getBytes());
    }
    /**
     *比较字符串text的MD5是否为hash
     */
    public static boolean hashEquals(String text, String hash) {
	boolean ret = false;
	if (text != null && hash != null) {
	    String tmp = hash(text);
	    ret = hash.equalsIgnoreCase(tmp);
	}
	return ret;
    }

    private static String hexString(byte[] b) {
	StringBuffer strBuffer = new StringBuffer();
	for (int i = 0; i < b.length; i += 1) {
	    strBuffer.append(hexDigits[(b[i] & 0xf0) >> 4]);
	    strBuffer.append(hexDigits[(b[i] & 0x0f)]);
	}
	return strBuffer.toString();
    }
}