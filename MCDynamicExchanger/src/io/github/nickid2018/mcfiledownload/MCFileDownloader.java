package io.github.nickid2018.mcfiledownload;

import io.github.nickid2018.DefaultConsoleLogger;
import io.github.nickid2018.I18N;
import io.github.nickid2018.argparser.CommandResult;
import io.github.nickid2018.util.SHACompute;
import io.github.nickid2018.util.download.DownloadService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.nickid2018.ProgramMain;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;

public class MCFileDownloader {

    public static String dir;
    public static boolean detailed;

    public static void downloadMCFileSimple(CommandResult result) {
        ProgramMain.logger = new DefaultConsoleLogger();
        try {
            dir = result.getStringOrDefault("--dir", new File("az").getAbsoluteFile().getParent());
            detailed = result.containsSwitch("-D");
            DownloadService.downloadResource("version_manifest",
                    "https://launchermeta.mojang.com/mc/game/version_manifest.json",
                    dir + "\\mc_version_manifest.json");
            DownloadService.startDownloadInfoOutput();
            DownloadService.waitDownloadOver();
            DownloadService.stopDownloadInfoOutput();
            String verjson = downloadVersionJSON(dir + "\\mc_version_manifest.json",
                    result.getSwitch("version").toString());
            if (verjson == null) {
                ProgramMain.logger.info(I18N.getText("error.downloadmc.version", result.getSwitch("version").toString()));
                return;
            }
            downloadFiles(verjson, result.getSwitch("version").toString());
            DownloadService.stopExecutors();
        } catch (Throwable e) {
            ProgramMain.logger.error(I18N.getText("error.unknown"), e);
        }
    }

    public static String downloadVersionJSON(String versionManifest, String version) throws Exception {
        JsonObject all = new JsonParser().parse(IOUtils.toString(new FileReader(versionManifest))).getAsJsonObject();
        JsonArray versions = all.get("versions").getAsJsonArray();
        String path = dir + "\\" + version + ".json";
        String downloadPage = null;
        if (version.equals("lasest-release"))
            version = all.get("lasest").getAsJsonObject().get("release").getAsString();
        if (version.equals("lasest-snapshot"))
            version = all.get("lasest").getAsJsonObject().get("snapshot").getAsString();
        for (JsonElement element : versions) {
            JsonObject object = element.getAsJsonObject();
            if (object.get("id").getAsString().equals(version)) {
                downloadPage = object.get("url").getAsString();
                break;
            }
        }
        if (downloadPage == null)
            return null;
        if (detailed)
            ProgramMain.logger.info(I18N.getText("download.versionjson", downloadPage));
        DownloadService.downloadResource("version_json", downloadPage, path);
        DownloadService.startDownloadInfoOutput();
        DownloadService.waitDownloadOver();
        DownloadService.stopDownloadInfoOutput();
        return path;
    }

    public static void downloadFiles(String versionJSON, String version) throws Exception {
        JsonObject all = new JsonParser().parse(IOUtils.toString(new FileReader(versionJSON))).getAsJsonObject();
        JsonObject downloads = all.getAsJsonObject("downloads");
        JsonObject clientMappingObject = downloads.getAsJsonObject("client_mappings");
        String clientMapping = clientMappingObject.get("url").getAsString();
        String clientMappingSHA1 = clientMappingObject.get("sha1").getAsString().toUpperCase();
        JsonObject jarObject = downloads.getAsJsonObject("client");
        String jar = jarObject.get("url").getAsString();
        String jarSHA1 = jarObject.get("sha1").getAsString().toUpperCase();
        if (detailed) {
            ProgramMain.logger.info(I18N.getText("download.mcjarfile", jar));
            ProgramMain.logger.info(I18N.getText("download.mcmap", clientMapping));
        }
        DownloadService.downloadResource("client_mappings", clientMapping,
                dir + "\\client_mappings_" + version + ".json");
        DownloadService.downloadResource("jar_file", jar, dir + "\\" + version + ".jar");
        DownloadService.startDownloadInfoOutput();
        DownloadService.waitDownloadOver();
        DownloadService.stopDownloadInfoOutput();
        String clientMappingCompute = SHACompute.getSHA1(dir + "\\client_mappings_" + version + ".json");
        String jarCompute = SHACompute.getSHA1(dir + "\\" + version + ".jar");
        if (!clientMappingCompute.equals(clientMappingSHA1) || !jarCompute.equals(jarSHA1)) {
            ProgramMain.logger.formattedInfo("error.downloadmc.sha1dismatch");
        } else
            ProgramMain.logger.formattedInfo("download.allover");
    }
}
