package com.github.nickid2018.util;

import java.io.*;
import java.net.*;
import org.apache.commons.io.*;

public class DownloadUtils {

	public static boolean downloadResource(String url, String to) {
		try {
			URL urlFile = new URL(url);
			File file = new File(to);
			if (!file.exists())
				file.createNewFile();
			System.out.println("Downloading resource \"" + url + "\"");
			URLConnection connect = urlFile.openConnection();
			CountInputStream is = new CountInputStream(connect.getInputStream());
			System.out.println("Connection has been created");
			Thread t = new Thread(() -> {
				try {
					OutputStream stream = new FileOutputStream(file);
					IOUtils.copy(is, stream);
					stream.close();
					System.out.println("Downloaded resource \"" + url + "\"");
				} catch (IOException e) {
					System.err.println("Cannot download resource \"" + url + "\"");
					e.printStackTrace();
					file.delete();
				}
			});
			t.start();
			while (t.isAlive()) {
				Thread.sleep(1000);
				is.getDeltaRead();
				System.out.println("Downloading Progress: " + getSize(is.getReaded()) + " readed, "
						+ getSize(connect.getContentLength()) + " in total, Speed: " + formatSpeed(is.getSpeed()));
			}
		} catch (Exception e) {
			System.err.println("Cannot download resource \"" + url + "\"");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static String formatSpeed(double speed) {
		if (speed <= 1024)
			return speed + "B/s";
		if (speed <= 1048576)
			return speed / 1024 + "KiB/s";
		if (speed <= 1073741824)
			return speed / 1073741824 + "MiB/s";
		return "Unbelievable Speed!!";
	}

	private static String getSize(double size) {
		if (size <= 1024)
			return size + "B";
		if (size <= 1048576)
			return size / 1024 + "KiB";
		if (size <= 1073741824)
			return size / 1073741824 + "MiB";
		return size / 1099511627776.0 + "GiB";
	}
}
