package com.qiniu.process.other;

import com.qiniu.model.qdora.VideoTS;
import com.qiniu.process.Base;
import com.qiniu.storage.Configuration;
import com.qiniu.util.RequestUtils;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class ExportTS extends Base<Map<String, String>> {

    private String domain;
    private String protocol;
    private String urlIndex;
    private Configuration configuration;
    private M3U8Manager m3U8Manager;

    public ExportTS(Configuration configuration, String domain, String protocol, String urlIndex) throws IOException {
        super("exportts", "", "", null);
        set(configuration, domain, protocol, urlIndex);
        this.m3U8Manager = new M3U8Manager(configuration.clone(), protocol);
    }

    public ExportTS(Configuration configuration, String domain, String protocol, String urlIndex, String savePath,
                    int saveIndex) throws IOException {
        super("exportts", "", "", null, savePath, saveIndex);
        set(configuration, domain, protocol, urlIndex);
        this.m3U8Manager = new M3U8Manager(configuration.clone(), protocol);
    }

    public ExportTS(Configuration configuration, String domain, String protocol, String urlIndex, String savePath)
            throws IOException {
        this(configuration, domain, protocol, urlIndex, savePath, 0);
    }

    private void set(Configuration configuration, String domain, String protocol, String urlIndex) throws IOException {
        this.configuration = configuration;
        if (urlIndex == null || "".equals(urlIndex)) {
            this.urlIndex = "url";
            if (domain == null || "".equals(domain)) {
                throw new IOException("please set one of domain and urlIndex.");
            } else {
                RequestUtils.lookUpFirstIpFromHost(domain);
                this.domain = domain;
                this.protocol = protocol == null || !protocol.matches("(http|https)") ? "http" : protocol;
            }
        } else {
            this.urlIndex = urlIndex;
        }
    }

    public ExportTS clone() throws CloneNotSupportedException {
        ExportTS exportTS = (ExportTS)super.clone();
        exportTS.m3U8Manager = new M3U8Manager(configuration.clone(), protocol);
        return exportTS;
    }

    @Override
    protected String resultInfo(Map<String, String> line) {
        return line.get(urlIndex);
    }

    @Override
    protected String singleResult(Map<String, String> line) throws Exception {
        String url = line.get(urlIndex);
        if (url == null || "".equals(url)) {
            String key = line.get("key");
            if (key == null) throw new IOException("key is not exists or empty in " + line);
            url = protocol + "://" + domain + "/" + key.replace("\\?", "%3f");
            line.put(urlIndex, url);
        }
        return String.join("\n", m3U8Manager.getVideoTSListByUrl(url).stream()
                .map(VideoTS::toString).collect(Collectors.toList()));
    }

    @Override
    public void closeResource() {
        super.closeResource();
        domain = null;
        protocol = null;
        urlIndex = null;
        configuration = null;
        m3U8Manager = null;
    }
}
