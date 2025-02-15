package com.qiniu.process.other;

import com.qiniu.process.Base;
import com.qiniu.storage.Configuration;
import com.qiniu.util.FileUtils;
import com.qiniu.util.RequestUtils;
import com.qiniu.util.StringMap;
import com.qiniu.util.URLUtils;

import java.io.IOException;
import java.util.Map;

public class DownloadFile extends Base<Map<String, String>> {

    private String domain;
    private String protocol;
    private String urlIndex;
    private String suffixOrQuery;
    private boolean useQuery;
    private String addPrefix;
    private String rmPrefix;
    private StringMap headers;
    private boolean preDown;
    private Configuration configuration;
    private HttpDownloader downloader;

    public DownloadFile(Configuration configuration, String domain, String protocol, String urlIndex, String suffixOrQuery,
                        String host, boolean preDown, String addPrefix, String rmPrefix, String savePath, int saveIndex)
            throws IOException {
        super("download", "", "", null, savePath, saveIndex);
        set(configuration, domain, protocol, urlIndex, suffixOrQuery, host, preDown, addPrefix, rmPrefix);
        downloader = configuration == null ? new HttpDownloader() : new HttpDownloader(configuration);
    }

    public DownloadFile(Configuration configuration, String domain, String protocol, String urlIndex, String suffixOrQuery,
                        String host, String downPath, String addPrefix, String rmPrefix) throws IOException {
        super("download", "", "", null);
        if (downPath == null || "".equals(downPath)) preDown = true;
        else this.savePath = FileUtils.realPathWithUserHome(downPath);
        set(configuration, domain, protocol, urlIndex, suffixOrQuery, host, preDown, addPrefix, rmPrefix);
        downloader = configuration == null ? new HttpDownloader() : new HttpDownloader(configuration);
    }

    public DownloadFile(Configuration configuration, String domain, String protocol, String urlIndex, String suffixOrQuery,
                        String host, boolean preDown, String addPrefix, String rmPrefix, String savePath) throws IOException {
        this(configuration, domain, protocol, urlIndex, suffixOrQuery, host, preDown, addPrefix, rmPrefix, savePath, 0);
    }

    private void set(Configuration configuration, String domain, String protocol, String urlIndex, String suffixOrQuery,
                     String host, boolean preDown, String addPrefix, String rmPrefix) throws IOException {
        this.configuration = configuration;
        if (urlIndex == null || "".equals(urlIndex)) {
            this.urlIndex = "url";
            if (domain == null || "".equals(domain)) {
                throw new IOException("please set one of domain and url-index.");
            } else {
                RequestUtils.lookUpFirstIpFromHost(domain);
                this.domain = domain;
                this.protocol = protocol == null || !protocol.matches("(http|https)") ? "http" : protocol;
            }
        } else {
            this.urlIndex = urlIndex;
        }
        this.suffixOrQuery = suffixOrQuery == null ? "" : suffixOrQuery;
        useQuery = !"".equals(this.suffixOrQuery);
        if (host != null && !"".equals(host)) {
            RequestUtils.lookUpFirstIpFromHost(host);
            headers = new StringMap().put("Host", host);
        }
        this.preDown = preDown;
        this.addPrefix = addPrefix == null ? "" : addPrefix;
        this.rmPrefix = rmPrefix;
    }

    public DownloadFile clone() throws CloneNotSupportedException {
        DownloadFile downloadFile = (DownloadFile)super.clone();
        downloadFile.downloader = configuration == null ? new HttpDownloader() : new HttpDownloader(configuration);
        return downloadFile;
    }

    @Override
    protected String resultInfo(Map<String, String> line) {
        return line.get("key") + "\t" + line.get(urlIndex);
    }

    @Override
    protected String singleResult(Map<String, String> line) throws Exception {
        String url = line.get(urlIndex);
        String key = line.get("key");
        if (url == null || "".equals(url)) {
            if (key == null || "".equals(key)) throw new IOException("key is not exists or empty in " + line);
            url = protocol + "://" + domain + "/" + key.replace("\\?", "%3f") + suffixOrQuery;
            line.put(urlIndex, url);
            key = addPrefix + FileUtils.rmPrefix(rmPrefix, key); // 目标文件名
        } else {
            if (key != null) key = addPrefix + FileUtils.rmPrefix(rmPrefix, key);
            else key = addPrefix + FileUtils.rmPrefix(rmPrefix, URLUtils.getKey(url));
            if (useQuery) {
                url = url + suffixOrQuery;
                line.put(urlIndex, url);
            }
        }
        line.put("key", key);
        if (preDown) {
            downloader.download(url, headers);
            return key + "\t" + url;
        } else {
            String filename = (fileSaveMapper == null ? savePath : fileSaveMapper.getSavePath()) + FileUtils.pathSeparator + key;
            downloader.download(url, filename, headers);
            return key + "\t" + url + "\t" + filename;
        }
    }

    @Override
    protected void parseSingleResult(Map<String, String> line, String result) {
        // do nothing
    }

    @Override
    public void closeResource() {
        super.closeResource();
        domain = null;
        protocol = null;
        urlIndex = null;
        addPrefix = null;
        rmPrefix = null;
        configuration = null;
        downloader = null;
    }
}
