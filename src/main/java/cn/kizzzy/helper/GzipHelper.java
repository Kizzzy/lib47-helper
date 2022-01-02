package cn.kizzzy.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipHelper {

    /**
     * gzip加密
     */
    public static byte[] gzip(byte[] data) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(bos);
        gzip.write(data);
        gzip.finish();
        gzip.close();
        byte[] ret = bos.toByteArray();
        bos.close();
        return ret;
    }

    /**
     * gzip解密
     */
    public static byte[] ungzip(byte[] data) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        GZIPInputStream gzip = new GZIPInputStream(bis);
        byte[] buf = new byte[1024];
        int num = -1;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((num = gzip.read(buf, 0, buf.length)) != -1) {
            bos.write(buf, 0, num);
        }
        gzip.close();
        bis.close();
        byte[] ret = bos.toByteArray();
        bos.flush();
        bos.close();
        return ret;
    }


    /**
     * gizp数据解压
     */
    public static String uncompress(byte[] bytes) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toString();
    }


    /**
     * gizp解压
     */
    public static byte[] unGzip(byte[] buf) throws IOException {
        GZIPInputStream gzi = null;
        ByteArrayOutputStream bos = null;
        try {
            gzi = new GZIPInputStream(new ByteArrayInputStream(buf));
            bos = new ByteArrayOutputStream(buf.length);
            int count = 0;
            byte[] tmp = new byte[2048];
            while ((count = gzi.read(tmp)) != -1) {
                bos.write(tmp, 0, count);
            }
            buf = bos.toByteArray();
        } finally {
            if (bos != null) {
                bos.flush();
                bos.close();
            }
            if (gzi != null)
                gzi.close();
        }
        return buf;
    }
}