package cn.kizzzy.helper;

import java.util.LinkedList;
import java.util.List;

public class MathHelper {
    
    /**
     * 判断整数n是否为偶数
     */
    public static boolean isOdd(int n) {
        return (n & 0x01) == 0;
    }
    
    /**
     * 获取整数n的第i位的值
     */
    public static boolean getBit(int n, int i) {
        return ((n & (1 << i)) != 0);
    }
    
    public static List<Integer> getBitOne(int value) {
        List<Integer> list = new LinkedList<>();
        for (int index = 0; (1 << index) <= value; ++index) {
            if (getBit(value, index)) {
                list.add(index);
            }
        }
        return list;
    }
    
    /**
     * 将整数n的第i位的值置为1
     */
    public static int setBitOne(int n, int i) {
        return (n | (1 << i));
    }
    
    /**
     * 将整数n的多位的值置为1
     */
    public static int setBitOne(int n, int... i) {
        for (int j : i)
            n = setBitOne(n, j);
        return n;
    }
    
    /**
     * 将整数n的第i位的值置为0
     */
    public static int setBitZero(int n, int i) {
        return (n & (~(1 << i)));
    }
    
    /**
     * 将整数n的多位的值置为0
     */
    public static int setBitZero(int n, int... i) {
        for (int j : i)
            n = setBitZero(n, j);
        return n;
    }
    
    /**
     * 任意一个
     */
    public static boolean anyOne(int val, int... expects) {
        if (expects != null && expects.length > 0) {
            for (int expect : expects) {
                if ((val >> expect & 0x1) == 0x1) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 任意一个
     */
    public static boolean noneOne(int val, int... expects) {
        if (expects != null && expects.length > 0) {
            for (int expect : expects) {
                if ((val >> expect & 0x1) == 0x1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    /**
     * 任意一个
     */
    public static boolean allOne(int val, int... expects) {
        if (expects != null && expects.length > 0) {
            for (int expect : expects) {
                if ((val >> expect & 0x1) != 0x1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    /**
     * 任意一个
     */
    public static boolean any(int val, int... expects) {
        if (expects != null) {
            for (int expect : expects) {
                if (val == expect) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 皆不符合
     */
    public static boolean none(int val, int... expects) {
        if (expects != null) {
            for (int expect : expects) {
                if (val == expect) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * 符合所有值
     */
    public static boolean all(int val, int... expects) {
        if (expects != null) {
            for (int expect : expects) {
                if (val != expect) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * 两数之间
     */
    public static boolean between(int n, int min, int max) {
        return min <= n && n <= max;
    }
    
    /**
     * 某区段开始
     */
    public static boolean startWith(int n, int min) {
        return (n & min) == min;
    }
    
    /**
     *
     */
    public static int byteToInt(byte b) {
        return b & 0xFF;
    }
    
    /**
     *
     */
    public static long intToLong(int value) {
        if (value < 0) {
            return (long) (Math.pow(2, 32) + value);
        }
        return value;
    }
    
    public static boolean is_power_of(int num, int base) {
        if (base <= 1 || num < 1) {
            return false;
        }
        
        while (num % base == 0) {
            num = num / base;
        }
        
        return num == 1;
    }
    
    public static double clamp01(double val) {
        return clamp(val, 0, 1);
    }
    
    public static double clamp(double val, double min, double max) {
        if (min > max) {
            double temp = max;
            max = min;
            min = temp;
        }
        
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    }
}
