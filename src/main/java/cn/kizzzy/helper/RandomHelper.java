package cn.kizzzy.helper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class RandomHelper {
    private static final Random random = new Random();
    
    public static int next(int minValue, int maxValue) {
        if (minValue < maxValue)
            return random.nextInt(maxValue - minValue) + minValue;
        return minValue;
    }
    
    public static <E> E randomListElement(List<E> list) {
        if (list == null)
            throw new NullPointerException();
        if (list.size() < 1)
            throw new IndexOutOfBoundsException();
        return list.get(random.nextInt(list.size()));
    }
    
    public static <E> E randomArrayElement(E[] array) {
        if (array == null)
            throw new NullPointerException();
        if (array.length < 1)
            throw new IndexOutOfBoundsException();
        return array[random.nextInt(array.length)];
    }
    
    public static <E> E randomSetElement(Set<E> set) {
        if (set == null)
            throw new NullPointerException();
        int index = random.nextInt(set.size());
        int i = 0;
        for (E e : set)
            if (index == i++)
                return e;
        return null;
    }
    
    public static <E> E randomElement(Collection<E> coll) {
        if (coll == null) {
            throw new NullPointerException();
        }
        if (coll.isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        int index = random.nextInt(coll.size());
        int i = 0;
        for (E e : coll) {
            if (index == i++) {
                return e;
            }
        }
        return null;
    }
    
    public static <K, V> Entry<K, V> randomMapEntry(Map<K, V> map) {
        if (map == null)
            throw new NullPointerException();
        int index = random.nextInt(map.size());
        int i = 0;
        for (Entry<K, V> entry : map.entrySet())
            if (index == i++)
                return entry;
        return null;
    }
}
