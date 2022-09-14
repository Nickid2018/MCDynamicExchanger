package io.github.nickid2018.mcde.util;

public interface ConsumerE<T> {

    void accept(T value) throws Exception;
}
