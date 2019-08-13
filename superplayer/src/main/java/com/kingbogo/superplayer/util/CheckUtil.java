package com.kingbogo.superplayer.util;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Kingbo
 * @date 2018/10/22
 */
public final class CheckUtil {

    private CheckUtil() {
    }

    public static boolean isEmpty(CharSequence str) {
        return isNull(str) || str.length() == 0;
    }

    public static boolean isEmpty(Object[] os) {
        return isNull(os) || os.length == 0;
    }

    public static boolean isEmpty(Collection<?> l) {
        return isNull(l) || l.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> m) {
        return isNull(m) || m.isEmpty();
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

}
