package com.github.nickid2018.util.download;

import java.io.*;
import java.net.*;
import com.github.nickid2018.*;
import org.apache.commons.io.*;

public class Downloader implements Runnable, Comparable<Downloader> {

	private DownloadEntry entry;
	private volatile boolean isActive;
	private volatile URLConnection connection;
	private volatile CountInputStream input;

	public Downloader(DownloadEntry entry) {
		this.entry = entry;
	}

	public boolean isActive() {
		return isActive;
	}

	public String getName() {
		return entry.name;
	}

	public double getProgress() {
		return input.getReaded();
	}

	public double getSpeed() {
		input.getDeltaRead();
		return input.getSpeed();
	}

	public double getTotal() {
		return connection.getContentLengthLong();
	}

	@Override
	public void run() {
		File file = new File(entry.destination);
		try {
			if (!file.exists())
				file.createNewFile();
			connection = entry.downloadURL.openConnection();
			input = new CountInputStream(connection.getInputStream());
			isActive = true;
			ProgramMain.logger.info(I18N.getText("download.start", entry.name));
			OutputStream stream = new FileOutputStream(file);
			IOUtils.copy(input, stream);
			stream.close();
			ProgramMain.logger.info(I18N.getText("download.over", entry.name));
		} catch (Exception e) {
			DownloadService.FAILED_DOWNLOAD.add(entry);
			ProgramMain.logger.error(I18N.getText("error.download", entry.name, e.getMessage()), e);
			if (file.exists())
				file.delete();
		} finally {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
			DownloadService.DOWNLOADERS.remove(this);
		}
	}

	@Override
	public int compareTo(Downloader o) {
		return entry.name.compareTo(o.getName());
	}
}
