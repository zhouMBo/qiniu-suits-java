package com.qiniu.interfaces;

import java.io.*;

public interface IResultOutput<T> {

    void setRetryTimes(int retryTimes);

    String getSavePath();

    String getPrefix();

    String getSuffix();

    void preAddWriter(String key);

    void addWriter(String key) throws IOException;

    void closeWriters();

    void writeToKey(String key, String item, boolean flush) throws IOException;

    void writeSuccess(String item, boolean flush) throws IOException;

    void writeError(String item, boolean flush) throws IOException;
}
