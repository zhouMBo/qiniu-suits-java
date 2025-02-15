package com.qiniu.util;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileUtils {

    private static final MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
    private static final FileNameMap fileNameMap = URLConnection.getFileNameMap();

    public static String contentType(String path) {
        String type = fileNameMap.getContentTypeFor(path);
        if (type == null) {
            type = mimetypesFileTypeMap.getContentType(path);
            if ("application/octet-stream".equals(type)) {
                try {
                    type = Files.probeContentType(Paths.get(path));
                    if (type == null) return "application/octet-stream";
                    else return type;
                } catch (IOException e) {
                    return "application/octet-stream";
                }
            } else {
                return type;
            }
        } else {
            return type;
        }
    }

    public static String contentType(File file) throws IOException {
        String type = fileNameMap.getContentTypeFor(file.getCanonicalPath());
        if (type == null) {
            type = mimetypesFileTypeMap.getContentType(file);
            if ("application/octet-stream".equals(type)) {
                try {
                    type = Files.probeContentType(file.toPath());
                    if (type == null) return "application/octet-stream";
                    else return type;
                } catch (IOException e) {
                    return "application/octet-stream";
                }
            } else {
                return type;
            }
        } else {
            return type;
        }
    }

    public static String pathSeparator = System.getProperty("file.separator");
    public static String userHome = System.getProperty("user.home");

    public static String realPathWithUserHome(String pathStr) throws IOException {
        if (pathStr == null || "".equals(pathStr)) throw new IOException("the path is empty.");
        if (pathStr.startsWith("~" + pathSeparator)) {
            return userHome + pathStr.substring(1);
        } else if (pathStr.startsWith("\\~") || pathStr.startsWith("\\-")) {
            return pathStr.substring(1);
        } else {
            return new File(pathStr).getCanonicalPath();
        }
    }

    public static String rmPrefix(String prefix, String name) throws IOException {
        if (name == null) throw new IOException("empty filename.");
        if (prefix == null || "".equals(prefix) || name.length() < prefix.length()) return name;
        return name.substring(0, prefix.length()).replace(prefix, "") + name.substring(prefix.length());
    }

    public static String addSuffix(String name, String suffix) {
        return name + suffix;
    }

    public static String addPrefix(String prefix, String name) {
        return prefix + name;
    }

    public static String addPrefixAndSuffixKeepExt(String prefix, String name, String suffix) {

        return prefix + addSuffixKeepExt(name, suffix);
    }

    public static String addSuffixKeepExt(String name, String suffix) {

        return addSuffixWithExt(name, suffix, null);
    }

    public static String addPrefixAndSuffixWithExt(String prefix, String name, String suffix, String ext) {
        return prefix + addSuffixWithExt(name, suffix, ext);
    }

    public static String replaceExt(String name, String ext) {
        return addSuffixWithExt(name, "", ext);
    }

    public static String addSuffixWithExt(String name, String suffix, String ext) {
        if (name == null) return null;
        String[] items = getNameItems(name);
        return items[0] + suffix + (ext != null && !"".equals(ext) ?  "." + ext :
                (items[1] == null || "".equals(items[1]) ? "" : "." + items[1]));
    }

    public static String[] getNameItems(String name) {
        String[] items = name.split("\\.");
        if (items.length < 2) {
            return new String[]{items[0], ""};
        }
        StringBuilder shortName = new StringBuilder();
        for (int i = 0; i < items.length - 1; i++) {
            shortName.append(items[i]).append(".");
        }
        return new String[]{shortName.toString().substring(0, shortName.length() - 1), items[items.length - 1]};
    }

    public static boolean mkDirAndFile(File filePath) throws IOException {
        File parent = filePath.getParentFile();
        boolean success = parent.exists();
        if (!success) {
            success = parent.mkdirs();
            if (!success) return false;
        }
        success = filePath.exists();
        if (!success) {
            return filePath.createNewFile();
        } else {
            return true;
        }
    }
}