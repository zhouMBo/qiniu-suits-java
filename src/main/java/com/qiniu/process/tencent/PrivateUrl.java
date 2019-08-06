package com.qiniu.process.tencent;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.region.Region;
import com.qiniu.interfaces.ILineProcess;
import com.qiniu.process.Base;
import com.qiniu.util.CloudAPIUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

public class PrivateUrl extends Base<Map<String, String>> {

    private String region;
    private Date expiration;
    private COSClient cosClient;
    private ILineProcess<Map<String, String>> nextProcessor;

    public PrivateUrl(String secretId, String secretKey, String bucket, String region, long expires,
                      String savePath, int saveIndex) throws IOException {
        super("tenprivate", secretId, secretKey, bucket, savePath, saveIndex);
        this.region = region;
        expiration = new Date(System.currentTimeMillis() + expires);
        cosClient = new COSClient(new BasicCOSCredentials(secretId, secretKey), new ClientConfig(new Region(region)));
        CloudAPIUtils.checkTencent(cosClient);
    }

    public PrivateUrl(String secretId, String secretKey, String bucket, String region, long expires) {
        super("tenprivate", secretId, secretKey, bucket);
        this.region = region;
        expiration = new Date(System.currentTimeMillis() + expires);
        cosClient = new COSClient(new BasicCOSCredentials(secretId, secretKey), new ClientConfig(new Region(region)));
        CloudAPIUtils.checkTencent(cosClient);
    }

    public PrivateUrl(String secretId, String secretKey, String bucket, String endpoint, long expires,
                      String savePath) throws IOException {
        this(secretId, secretKey, bucket, endpoint, expires, savePath, 0);
    }

    public void updateRegion(String endpoint) {
        this.region = endpoint;
    }

    public void updateExpires(long expires) {
        this.expiration = new Date(System.currentTimeMillis() + expires);
    }

    public void setNextProcessor(ILineProcess<Map<String, String>> nextProcessor) {
        this.nextProcessor = nextProcessor;
        if (nextProcessor != null) processName = nextProcessor.getProcessName() + "_with_" + processName;
    }

    public PrivateUrl clone() throws CloneNotSupportedException {
        PrivateUrl cosPrivateUrl = (PrivateUrl)super.clone();
        cosPrivateUrl.cosClient = new COSClient(new BasicCOSCredentials(authKey1, authKey2), new ClientConfig(new Region(region)));
        if (nextProcessor != null) cosPrivateUrl.nextProcessor = nextProcessor.clone();
        return cosPrivateUrl;
    }

    @Override
    public String resultInfo(Map<String, String> line) {
        return line.get("key");
    }

    @Override
    public String singleResult(Map<String, String> line) throws Exception {
        String key = line.get("key");
        if (key == null) throw new IOException("key is not exists or empty in " + line);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = cosClient.generatePresignedUrl(bucket, key, expiration);
        if (nextProcessor != null) {
            line.put("url", url.toString());
            return nextProcessor.processLine(line);
        }
        return key + "\t" + url.toString();
    }

    @Override
    public void closeResource() {
        super.closeResource();
        region = null;
        expiration = null;
        cosClient = null;
        if (nextProcessor != null) nextProcessor.closeResource();
        nextProcessor = null;
    }
}
