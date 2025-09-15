package cn.elytra.mod.gtmqol.util;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class MemoizeUtils {

    public static <T, R> CleanableMemoizedFunction<T, R> memoize(Function<T, R> function) {
        return new MemoizedFunctionImpl<>(function);
    }

    private static class MemoizedFunctionImpl<T, R> implements CleanableMemoizedFunction<T, R> {

        private final Function<T, R> delegate;
        /// cache of the obtained values, probably an empty Optional if the function returned `null` before.
        private final ConcurrentMap<T, Optional<R>> cache = new ConcurrentHashMap<>();

        public MemoizedFunctionImpl(Function<T, R> delegate) {
            this.delegate = delegate;
        }

        @Override
        public R apply(T t) {
            return cache.computeIfAbsent(t, delegate.andThen(Optional::ofNullable)).orElse(null);
        }

        @Override
        public void clear() {
            cache.clear();
        }
    }

}
