package cn.kizzzy.helper;

public class ColorHelper {

    public static int merge(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }

    public static int argb2rgb(int argb) {
        return argb & 0xFFFFFF;
    }

    public static int rValue(int rgb) {
        return (rgb & 0xFF0000) >> 16;
    }

    public static int gValue(int rgb) {
        return (rgb & 0x00FF00) >> 8;
    }

    public static int bValue(int rgb) {
        return (rgb & 0x0000FF) >> 0;
    }

    public static int similarity(int val1, int val2) {
        int diff1 = Math.abs(rValue(val1) - rValue(val2));
        int diff2 = Math.abs(gValue(val1) - gValue(val2));
        int diff3 = Math.abs(bValue(val1) - bValue(val2));
        return Math.round((diff1 + diff2 + diff3) / 3.0f);
    }

    public static boolean similarity(int val1, int val2, int similarity) {
        return similarity(val1, val2) <= similarity;
    }
}
