package cn.kizzzy.helper;

import java.io.*;

public class ByteHelper {

    public static byte[] object2bytes(Object obj) {
        byte[] b = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos);) {
            oos.writeObject(obj);
            b = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static Object bytes2object(byte[] bs) {
        Object obj = null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bs);
             ObjectInputStream ois = new ObjectInputStream(bais);) {
            obj = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    public static int bytesToInt(byte[] src) {
        return (src[0] & 0xFF) | ((src[1] & 0xFF) << 8) | ((src[2] & 0xFF) << 16) | ((src[3] & 0xFF) << 24);
    }

    public static int bytesToInt(short[] src) {
        return (src[0] & 0xFF) | ((src[1] & 0xFF) << 8) | ((src[2] & 0xFF) << 16) | ((src[3] & 0xFF) << 24);
    }

    public static byte[] intsToBytes(int[] ints) {
        byte[] bytes = new byte[ints.length];
        for (int i = 0, n = ints.length; i < n; ++i) {
            if (ints[i] > 127) {
                bytes[i] = (byte) (ints[i] - 256);
            } else {
                bytes[i] = (byte) ints[i];
            }
        }
        return bytes;
    }

    public static int[] hexToByteArray(String hex) {
        return hexToByteArray(hex, " ", false);
    }

    public static int[] hexToByteArray(String hex, String split, boolean reverse) {
        String[] hexes = hex.split(" ");
        int[] array = new int[hexes.length];
        for (int i = 0, n = hexes.length; i < n; ++i) {
            String str = hexes[i];
            if (reverse) {
                str = (i & 0x01) == 1 ? hexes[i - 1] : hexes[i + 1];
            }
            array[i] = Integer.parseInt(str, 16);
        }
        return array;
    }

    public static void printArray(int[] array) {
        for (int i : array) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
