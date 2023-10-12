package cn.kizzzy.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZlibHelper {
    
    /**
     * zlib压缩
     */
    public static byte[] compress(byte[] input) throws IOException {
        return compress(input, false);
    }
    
    /**
     * zlib压缩
     */
    public static byte[] compress(byte[] input, boolean nowrap) throws IOException {
        return compress(input, 0, input.length, nowrap);
    }
    
    /**
     * zlib压缩
     */
    public static byte[] compress(byte[] input, int offset, int length) throws IOException {
        return compress(input, offset, length, false);
    }
    
    /**
     * zlib压缩
     */
    public static byte[] compress(byte[] input, int offset, int length, boolean nowrap) throws IOException {
        Deflater deflater = new Deflater(8, nowrap);
        deflater.setInput(input, offset, length);
        deflater.finish();
        
        byte[] buf = new byte[8192];
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            while (!deflater.finished()) {
                int count = deflater.deflate(buf);
                bos.write(buf, 0, count);
            }
            deflater.end();
            return bos.toByteArray();
        }
    }
    
    /**
     * zlib解压
     */
    public static byte[] uncompress(byte[] data) throws DataFormatException, IOException {
        return uncompress(data, false);
    }
    
    /**
     * zlib解压
     */
    public static byte[] uncompress(byte[] data, boolean nowrap) throws DataFormatException, IOException {
        return uncompress(data, 0, data.length, nowrap);
    }
    
    /**
     * zlib解压
     */
    public static byte[] uncompress(byte[] data, int offset, int length) throws DataFormatException, IOException {
        return uncompress(data, offset, length, false);
    }
    
    /**
     * zlib解压
     */
    public static byte[] uncompress(byte[] data, int offset, int length, boolean nowrap) throws IOException, DataFormatException {
        Inflater inflater = new Inflater(nowrap);
        inflater.setInput(data, offset, length);
        
        byte[] buf = new byte[8192];
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(length)) {
            while (!inflater.finished()) {
                int count = inflater.inflate(buf);
                bos.write(buf, 0, count);
            }
            inflater.end();
            return bos.toByteArray();
        }
    }
    
    /**
     * 计算压缩后最大字节数
     */
    public static int zlibBound(int sourceLen) {
        return sourceLen + (sourceLen >> 12) + (sourceLen >> 14) + (sourceLen >> 25) + 13;
    }
    
    public static byte[] unZip(byte[] data) throws IOException {
        byte[] buf = new byte[8192];
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ZipInputStream zis = new ZipInputStream(bis)) {
            while (zis.getNextEntry() != null) {
                for (int n; (n = zis.read(buf, 0, buf.length)) != -1; ) {
                    bos.write(buf, 0, n);
                }
            }
            return bos.toByteArray();
        }
    }
    
    public static byte[] zip(byte[] data) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(bos)) {
            ZipEntry zipEntry = new ZipEntry("zip");
            zipEntry.setSize(data.length);
            zos.putNextEntry(zipEntry);
            zos.write(data);
            zos.closeEntry();
            return bos.toByteArray();
        }
    }
}
