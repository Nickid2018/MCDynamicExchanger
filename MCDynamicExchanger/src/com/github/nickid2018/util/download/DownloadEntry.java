package com.github.nickid2018.util.download;

import java.net.MalformedURLException;
import java.net.URL;

public class DownloadEntry {

    public static final DownloadEntry NULL = new DownloadEntry();
    public String name;
    public URL downloadURL;
    public String destination;

    private DownloadEntry() {
    }

    public DownloadEntry(String name, String url, String dest) throws MalformedURLException {
        this.name = name;
        downloadURL = new URL(url);
        destination = dest;
    }
}
