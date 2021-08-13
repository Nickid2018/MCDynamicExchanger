package com.github.nickid2018.util;

import java.util.Objects;

@FunctionalInterface
public interface BiExConsumer<T, U> {

    void accept(T t, U u) throws Exception;

    default BiExConsumer<T, U> andThen(BiExConsumer<? super T, ? super U> after) {
        Objects.requireNonNull(after);

        return (l, r) -> {
            accept(l, r);
            after.accept(l, r);
        };
    }
}
