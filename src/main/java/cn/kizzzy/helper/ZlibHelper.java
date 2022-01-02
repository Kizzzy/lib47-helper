package cn.kizzzy.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.*;

public class ZlibHelper {

    /**
     * 计算压缩后最大字节数
     */
    public static int zlibBound(int sourceLen) {
        return sourceLen + (sourceLen >> 12) + (sourceLen >> 14) + (sourceLen >> 25) + 13;
    }

    /**
     * zlib压缩
     */
    public static byte[] compress(byte[] inputData) {
        return compress(inputData, 0, inputData.length);
    }

    /**
     * zlib压缩
     */
    public static byte[] compress(byte[] inputData, int offset, int length) {
        Deflater deflater = new Deflater();
        deflater.setInput(inputData, offset, length);
        deflater.finish();

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(inputData.length)) {
            byte[] buf = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buf);
                bos.write(buf, 0, count);
            }
            deflater.end();
            return bos.toByteArray();
        } catch (IOException e) {
            LogHelper.error(null, e);
        }
        return null;
    }

    /**
     * zlib解压
     */
    public static byte[] uncompress(byte[] data) {
        return uncompress(data, 0, data.length);
    }

    /**
     * zlib解压
     */
    public static byte[] uncompress(byte[] data, int offset, int length) {
        Inflater inflater = new Inflater();
        inflater.setInput(data, offset, length);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(length)) {
            byte[] buf = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buf);
                baos.write(buf, 0, count);
            }
            inflater.end();
            return baos.toByteArray();
        } catch (Exception e) {
            LogHelper.error(null, e);
        }
        return new byte[0];
    }

    public static byte[] unZip(byte[] data) {
        byte[] bArr2 = null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ZipInputStream zis = new ZipInputStream(bais)) {
            while (zis.getNextEntry() != null) {
                byte[] buf = new byte[1024];

                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    while (true) {
                        int read = zis.read(buf, 0, buf.length);
                        if (read == -1) {
                            break;
                        }
                        baos.write(buf, 0, read);
                    }
                    bArr2 = baos.toByteArray();
                    baos.flush();
                }
            }
        } catch (Exception e) {
            LogHelper.error(null, e);
        }
        return null;
    }

    public static byte[] zip(byte[] data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            ZipEntry zipEntry = new ZipEntry("zip");
            zipEntry.setSize(data.length);
            zos.putNextEntry(zipEntry);
            zos.write(data);
            zos.closeEntry();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
