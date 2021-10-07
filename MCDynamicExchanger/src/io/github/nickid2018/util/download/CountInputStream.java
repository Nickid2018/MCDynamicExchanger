package io.github.nickid2018.util.download;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class CountInputStream extends InputStream {

    private final InputStream stream;
    private final AtomicInteger read = new AtomicInteger(0);
    private final AtomicInteger deltaRead = new AtomicInteger(0);
    private long time = System.currentTimeMillis();
    private double speed;

    public CountInputStream(InputStream is) {
        stream = is;
    }

    public int getRead() {
        return read.get();
    }

    public void getDeltaRead() {
        int deltaRead = this.deltaRead.getAndSet(0);
        speed = deltaRead / (double) (System.currentTimeMillis() - time) * 1000.0;
        time = System.currentTimeMillis();
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public int read() throws IOException {
        read.incrementAndGet();
        deltaRead.incrementAndGet();
        return stream.read();
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
