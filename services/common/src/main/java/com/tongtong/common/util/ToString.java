package com.tongtong.common.util;

import java.util.Collection;

/**
 * ToString class provides toString representation for Collection classes
 */
public class ToString {
    public static <T> String toString(Collection<T> items) {
        StringBuilder builder = new StringBuilder("[");
        if (items != null) {
            int count = 0;
            for (T item : items) {
                builder.append(item.toString());
                if (++count < items.size()) {
                    builder.append(", ");
                }
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
