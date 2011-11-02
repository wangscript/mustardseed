package org.mustardseed.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;

/**
 *对数据流操作的工具类
 *@author HermitWayne
 */
public class StreamUtils {
    private static final int bufferSize = 4 * 1024;

    /**
     *数据流拷贝
     */
    public static long copyStream(InputStream in, OutputStream out) {
	long ret = 0;
	if (in == null || out == null)
	    return ret;
	BufferedInputStream bin = new BufferedInputStream(in);
	BufferedOutputStream bout = new BufferedOutputStream(out);
	
	byte[] buffer = new byte[bufferSize];
	
	int n = 0;

	try {
	    while((n = bin.read(buffer)) != -1){
		bout.write(buffer);
		ret += n;
	    }
	    bout.flush();
	} catch (IOException e) {}
	close(bin);
	close(bout);
	return ret;
    }
    /**
     *关闭数据
     */
    public static boolean close(Closeable item) {
	boolean ret = true;
	try {
	    item.close();
	} catch (IOException e) {
	    ret = false;
	}
	return ret;
    }
}