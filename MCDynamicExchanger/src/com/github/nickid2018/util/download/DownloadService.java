package com.github.nickid2018.util.download;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
import com.github.nickid2018.*;

public class DownloadService {

	public static final int MAX_DOWNLOAD_THREADS = 4;
	public static final ExecutorService DOWNLOADERS_EXECUTOR = Executors.newFixedThreadPool(MAX_DOWNLOAD_THREADS);
	public static final Set<Downloader> DOWNLOADERS = new ConcurrentSkipListSet<>();
	public static final Queue<DownloadEntry> FAILED_DOWNLOAD = new LinkedBlockingQueue<>();

	private static Thread infoOutputer;
	private static volatile boolean signExit;

	public static boolean waitDownloadOver() {
		for (int i = 0; i < 60000; i++) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			if (DOWNLOADERS.isEmpty())
				return true;
		}
		return false;
	}

	public static void waitAndExit() {
		try {
			DOWNLOADERS_EXECUTOR.awaitTermination(60000, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		System.exit(0);
	}

	public static void downloadResource(String name, String url, String to) {
		try {
			DownloadEntry entry = new DownloadEntry(name, url, to);
			Downloader downloader = new Downloader(entry);
			DOWNLOADERS.add(downloader);
			DOWNLOADERS_EXECUTOR.execute(downloader);
		} catch (IOException e) {
			FAILED_DOWNLOAD.add(DownloadEntry.NULL);
			ProgramMain.logger.error(I18N.getText("error.download", name, e.getMessage()), e);
		}
	}

	public static void startDownloadInfoOutput() {
		signExit = false;
		infoOutputer = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				if (signExit)
					break;
				Set<Downloader> actives = DOWNLOADERS.stream().filter(downloader -> downloader.isActive())
						.collect(Collectors.toSet());
				if (!actives.isEmpty()) {
					StringBuilder sb = new StringBuilder();
					for (Downloader downloader : actives) {
						sb.append(downloader.getName());
						sb.append(" ");
						sb.append(getSize(downloader.getProgress()));
						sb.append("/");
						sb.append(getSize(downloader.getTotal()));
						sb.append(" ");
						sb.append(formatSpeed(downloader.getSpeed()));
						sb.append(" | ");
					}
					ProgramMain.logger.info(sb.substring(0, sb.length() - 2));
					ProgramMain.logger.flush();
				}
			}
		});
		infoOutputer.start();
	}

	public static void stopDownloadInfoOutput() {
		signExit = true;
	}

	public static void stopExecutors() {
		DOWNLOADERS_EXECUTOR.shutdownNow();
	}

	public static String formatSpeed(double speed) {
		if (speed <= 1024 * 2)
			return dealWithDouble(speed) + "B/s";
		if (speed <= 1048576 * 2)
			return dealWithDouble(speed / 1024) + "KiB/s";
		if (speed <= 1073741824 * 2.0)
			return dealWithDouble(speed / 1048576) + "MiB/s";
		return "Unbelievable Speed!!";
	}

	public static String getSize(double size) {
		if (size <= 1024 * 2)
			return dealWithDouble(size) + "B";
		if (size <= 1048576 * 2)
			return dealWithDouble(size / 1024) + "KiB";
		if (size <= 1073741824 * 2.0)
			return dealWithDouble(size / 1048576) + "MiB";
		return dealWithDouble(size / 1073741824) + "GiB";
	}

	private static double dealWithDouble(double in) {
		return (int) (in * 10) / 10.0;
	}
}
