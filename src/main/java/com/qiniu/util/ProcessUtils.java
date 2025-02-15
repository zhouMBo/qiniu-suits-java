package com.qiniu.util;

import java.util.HashSet;
import java.util.Set;

public final class ProcessUtils {

    public static Set<String> needUrlProcesses = new HashSet<String>(){{
        add("asyncfetch");
        add("privateurl");
        add("qhash");
        add("avinfo");
        add("exportts");
        add("tenprivate");
        add("awsprivate");
        add("aliprivate");
        add("download");
        add("imagecensor");
        add("videocensor");
    }};
    public static Set<String> needToKeyProcesses = new HashSet<String>(){{
        add("copy");
        add("move");
        add("rename");
    }};
    public static Set<String> needFopsProcesses = new HashSet<String>(){{
        add("pfop");
    }};
    public static Set<String> needIdProcesses = new HashSet<String>(){{
        add("pfopresult");
        add("censorresult");
    }};
    public static Set<String> needAvinfoProcesses = new HashSet<String>(){{
        add("pfopcmd");
    }};
    public static Set<String> qiniuProcessesWithBucket = new HashSet<String>(){{
        add("status");
        add("type");
        add("lifecycle");
        add("mirror");
        add("delete");
        add("copy");
        add("rename");
        add("move");
        add("pfop");
        add("stat");
    }};
    public static Set<String> canBatchProcesses = new HashSet<String>(){{
        add("status");
        add("type");
        add("lifecycle");
        add("copy");
        add("move");
        add("rename");
        add("delete");
        add("stat");
    }};
    public static Set<String> tenProcesses = new HashSet<String>(){{
        add("tenprivate");
    }};
    public static Set<String> awsProcesses = new HashSet<String>(){{
        add("awsprivate");
    }};
    public static Set<String> aliProcesses = new HashSet<String>(){{
        add("aliprivate");
    }};
    public static Set<String> needBucketProcesses = new HashSet<String>(){{
        addAll(qiniuProcessesWithBucket);
        addAll(tenProcesses);
        addAll(awsProcesses);
        addAll(aliProcesses);
    }};
    public static Set<String> needQiniuAuthProcesses = new HashSet<String>(){{
        addAll(qiniuProcessesWithBucket);
        add("asyncfetch");
        add("privateurl");
        add("imagecensor");
        add("videocensor");
        add("censorresult");
    }};
    public static Set<String> supportStorageSource = new HashSet<String>(){{
        addAll(needQiniuAuthProcesses);
        addAll(needBucketProcesses);
        add("qhash");
        add("avinfo");
        add("exportts");
        add("download");
        add("filter");
    }};
    public static Set<String> dangerousProcesses = new HashSet<String>(){{
        add("status");
        add("lifecycle");
        add("move");
        add("rename");
        add("delete");
    }};
//    public static Set<String> processes = new HashSet<String>(){{
//        addAll(needUrlProcesses);
//        addAll(needToKeyProcesses);
//        addAll(needFopsProcesses);
//        addAll(needPidProcesses);
//        addAll(needAvinfoProcesses);
//        addAll(needBucketAnKeyProcesses);
//        addAll(supportListSourceProcesses);
//    }};
    public static Set<String> canPrivateToNextProcesses = new HashSet<String>(){{
        add("asyncfetch");
        add("download");
        add("imagecensor");
        add("videocensor");
    }};

    public static boolean needUrl(String process) {
        return needUrlProcesses.contains(process);
    }

    public static boolean needToKey(String process) {
        return needToKeyProcesses.contains(process);
    }

    public static boolean needFops(String process) {
        return needFopsProcesses.contains(process);
    }

    public static boolean needId(String process) {
        return needIdProcesses.contains(process);
    }

    public static boolean needAvinfo(String process) {
        return needAvinfoProcesses.contains(process);
    }

    public static boolean needBucket(String process) {
        return needBucketProcesses.contains(process);
    }

    public static boolean needQiniuAuth(String process) {
        return needQiniuAuthProcesses.contains(process);
    }

    public static boolean needTencentAuth(String process) {
        return tenProcesses.contains(process);
    }

    public static boolean needAliyunAuth(String process) {
        return aliProcesses.contains(process);
    }

    public static boolean needAwsS3Auth(String process) {
        return awsProcesses.contains(process);
    }

    public static boolean canBatch(String process) {
        return canBatchProcesses.contains(process);
    }

    public static boolean supportStorageSource(String process) {
        return supportStorageSource.contains(process);
    }

    public static boolean isDangerous(String process) {
        return dangerousProcesses.contains(process);
    }

//    public static boolean isSupportedProcess(String process) {
//        return processes.contains(process);
//    }

    public static boolean canPrivateToNext(String process) {
        return canPrivateToNextProcesses.contains(process);
    }
}
