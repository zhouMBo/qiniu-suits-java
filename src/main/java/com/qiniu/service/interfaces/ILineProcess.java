package com.qiniu.service.interfaces;

import com.qiniu.common.QiniuException;

import java.util.List;
import java.util.Map;

public interface ILineProcess<T> {

    ILineProcess<T> clone() throws CloneNotSupportedException;

    default String getProcessName() {
        return "line_process";
    }

    default void setRetryCount(int retryCount) {}

    default void setBatch(boolean batch) {}

    default void setResultTag(String resultTag) {}

    void processLine(List<T> list) throws QiniuException;

    default void setNextProcessor(ILineProcess<Map<String, String>> nextProcessor) {}

    void closeResource();
}
