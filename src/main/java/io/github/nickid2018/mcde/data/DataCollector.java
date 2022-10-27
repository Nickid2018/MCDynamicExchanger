package io.github.nickid2018.mcde.data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataCollector {

    private static final Map<String, Queue<DataEntry<?>>> DATA = new ConcurrentHashMap<>();
    private static final Map<String, Long> DATA_TIMEOUT = new ConcurrentHashMap<>();
    private static final Map<String, Integer> DATA_THRESHOLDS = new ConcurrentHashMap<>();

    private static void putEntry(String name, DataEntry<?> entry) {
        Queue<DataEntry<?>> queue = DATA.computeIfAbsent(name, k -> new ConcurrentLinkedQueue<>());
        queue.offer(entry);
        if (queue.size() > DATA_THRESHOLDS.getOrDefault(name, 1000))
            queue.poll();
    }

    private static void putEntryWithTimeout(String name, DataEntry<?> entry, long timeout) {
        if (System.currentTimeMillis() - DATA_TIMEOUT.getOrDefault(name, 0L) > timeout) {
            DATA_TIMEOUT.put(name, System.currentTimeMillis());
            putEntry(name, entry);
        }
    }

    public static <T> void put(String name, T value) {
        putEntry(name, new DataEntry<>(value));
    }

    public static void put(String name, int value) {
        putEntry(name, new DataEntry<>(value));
    }

    public static void put(String name, long value) {
        putEntry(name, new DataEntry<>(value));
    }

    public static void put(String name, float value) {
        putEntry(name, new DataEntry<>(value));
    }

    public static void put(String name, double value) {
        putEntry(name, new DataEntry<>(value));
    }

    public static void put(String name, boolean value) {
        putEntry(name, new DataEntry<>(value));
    }

    public static void put(String name, char value) {
        putEntry(name, new DataEntry<>(value));
    }

    public static void put(String name, byte value) {
        putEntry(name, new DataEntry<>(value));
    }

    public static void put(String name, short value) {
        putEntry(name, new DataEntry<>(value));
    }

    public static <T> void put(Object name, T value) {
        putEntry(name.toString(), new DataEntry<>(value));
    }

    public static void put(Object name, int value) {
        putEntry(name.toString(), new DataEntry<>(value));
    }

    public static void put(Object name, long value) {
        putEntry(name.toString(), new DataEntry<>(value));
    }

    public static void put(Object name, float value) {
        putEntry(name.toString(), new DataEntry<>(value));
    }

    public static void put(Object name, double value) {
        putEntry(name.toString(), new DataEntry<>(value));
    }

    public static void put(Object name, boolean value) {
        putEntry(name.toString(), new DataEntry<>(value));
    }

    public static void put(Object name, char value) {
        putEntry(name.toString(), new DataEntry<>(value));
    }

    public static void put(Object name, byte value) {
        putEntry(name.toString(), new DataEntry<>(value));
    }

    public static void put(Object name, short value) {
        putEntry(name.toString(), new DataEntry<>(value));
    }

    public static <T> void putWithTimeout(String name, T value, long timeout) {
        putEntryWithTimeout(name, new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(String name, int value, long timeout) {
        putEntryWithTimeout(name, new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(String name, long value, long timeout) {
        putEntryWithTimeout(name, new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(String name, float value, long timeout) {
        putEntryWithTimeout(name, new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(String name, double value, long timeout) {
        putEntryWithTimeout(name, new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(String name, boolean value, long timeout) {
        putEntryWithTimeout(name, new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(String name, char value, long timeout) {
        putEntryWithTimeout(name, new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(String name, byte value, long timeout) {
        putEntryWithTimeout(name, new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(String name, short value, long timeout) {
        putEntryWithTimeout(name, new DataEntry<>(value), timeout);
    }

    public static <T> void putWithTimeout(Object name, T value, long timeout) {
        putEntryWithTimeout(name.toString(), new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(Object name, int value, long timeout) {
        putEntryWithTimeout(name.toString(), new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(Object name, long value, long timeout) {
        putEntryWithTimeout(name.toString(), new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(Object name, float value, long timeout) {
        putEntryWithTimeout(name.toString(), new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(Object name, double value, long timeout) {
        putEntryWithTimeout(name.toString(), new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(Object name, boolean value, long timeout) {
        putEntryWithTimeout(name.toString(), new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(Object name, char value, long timeout) {
        putEntryWithTimeout(name.toString(), new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(Object name, byte value, long timeout) {
        putEntryWithTimeout(name.toString(), new DataEntry<>(value), timeout);
    }

    public static void putWithTimeout(Object name, short value, long timeout) {
        putEntryWithTimeout(name.toString(), new DataEntry<>(value), timeout);
    }

    public static void storeNowStack(String name) {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        StackTraceElement[] newStack = new StackTraceElement[stack.length - 1];
        System.arraycopy(stack, 1, newStack, 0, newStack.length);
        putEntry(name, new DataEntry<>(newStack));
    }

    public static void storeNowStackOnce(String name) {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        StackTraceElement[] newStack = new StackTraceElement[stack.length - 1];
        System.arraycopy(stack, 1, newStack, 0, newStack.length);
        putEntryWithTimeout(name, new DataEntry<>(newStack), Long.MAX_VALUE);
    }

    public static void setDataThreshold(String name, int threshold) {
        DATA_THRESHOLDS.put(name, threshold);
    }

    public static Set<String> getKeys() {
        return new TreeSet<>(DATA.keySet());
    }

    public static List<DataEntry<?>> getData(String name) {
        return DATA.containsKey(name) ? new ArrayList<>(DATA.get(name)) : new ArrayList<>();
    }

    public static void clear(String name) {
        DATA.remove(name);
    }

    public static void clear() {
        DATA.clear();
    }
}
