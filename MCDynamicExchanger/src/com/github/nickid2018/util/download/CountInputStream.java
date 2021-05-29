package com.github.nickid2018.util.download;

import java.io.*;

public class CountInputStream extends InputStream {

	private InputStream stream;
	private volatile int readed = 0;
	private volatile int deltaRead = 0;
	private long time = System.currentTimeMillis();
	private double speed;

	public CountInputStream(InputStream is) {
		stream = is;
	}

	public int getReaded() {
		return readed;
	}

	public int getDeltaRead() {
		int deltaReaded = deltaRead;
		deltaRead = 0;
		speed = deltaReaded / (double) (System.currentTimeMillis() - time) * 1000.0;
		time = System.currentTimeMillis();
		return deltaReaded;
	}

	public double getSpeed() {
		return speed;
	}

	@Override
	public int read() throws IOException {
		readed += 1;
		deltaRead += 1;
		return stream.read();
	}

	@Override
	public void close() throws IOException {
		stream.close();
	}
}
