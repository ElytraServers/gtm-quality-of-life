package cn.elytra.mod.gtmqol.util;

import java.util.function.Function;

public interface CleanableMemoizedFunction<T, R> extends Function<T, R>, AutoCloseable {

    default void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void close() {
        clear();
    }
}
