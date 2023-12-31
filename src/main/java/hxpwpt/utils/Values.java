package hxpwpt.utils;

import javax.annotation.Nonnull;

public abstract class Values {
    @Nonnull
    public static <T> T nonnull (T obj, String name) {
        if (obj == null) {
            throw new NullPointerException (name + " is expected to be non-null");
        }
        return obj;
    }
    @Nonnull
    public static <T> T nonnull (T obj) {
        return nonnull (obj, "This object");
    }
}
