package io.github.nickid2018.mcde.data;

public class DataEntry<T> {
    private final T value;

    private final long time = System.nanoTime();

    public DataEntry(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public long getTime() {
        return time;
    }
}
