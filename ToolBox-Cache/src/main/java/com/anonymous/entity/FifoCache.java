package com.anonymous.entity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 *     一个固定大小的基于FIFO替换策略的缓存
 *
 * @author 来源于网络：https://www.pdai.tech/md/java/collection/java-map-LinkedHashMap&LinkedHashSet.html
 * @version 1.0
 * @date 2021/11/5 13:36
 */
public class FifoCache<K, V> extends LinkedHashMap<K, V> {

    /**
     * 初始容量
     */
    private final int cacheSize;

    public FifoCache(int cacheSize){
        this.cacheSize = cacheSize;
    }

    /**
     * 该方法的作用是告诉Map是否要删除“最老”的Entry，所谓最老就是当前Map中最早插入的Entry
     * 如果该方法返回true，最老的那个元素就会被删除。
     * 在每次插入新元素的之后LinkedHashMap会自动询问removeEldestEntry()是否要删除最老的元素。
     *
     * @param eldest
     * @return true 删除
     *         false 不删除
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() > cacheSize;
    }
}

