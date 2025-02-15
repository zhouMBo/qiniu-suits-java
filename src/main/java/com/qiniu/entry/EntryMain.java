package com.qiniu.entry;

import com.qiniu.config.ParamsConfig;
import com.qiniu.config.PropertiesFile;
import com.qiniu.interfaces.IDataSource;
import com.qiniu.datasource.InputSource;
import com.qiniu.interfaces.IEntryParam;
import com.qiniu.interfaces.ILineProcess;
import com.qiniu.util.ParamsUtils;
import com.qiniu.util.ProcessUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EntryMain {

    public static boolean process_verify = true;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        Map<String, String> preSetMap = new HashMap<String, String>(){{
            put("f", "verify=false");
            put("s", "single=true");
            put("single", "single=true");
            put("line", "single=true");
            put("i", "interactive=true");
            put("interactive", "interactive=true");
        }};
        Map<String, String> paramsMap = getEntryParams(args, preSetMap);
        if (paramsMap.containsKey("verify")) process_verify = Boolean.parseBoolean(paramsMap.get("verify"));
        boolean single = paramsMap.containsKey("single") && Boolean.parseBoolean(paramsMap.get("single"));
        boolean interactive = paramsMap.containsKey("interactive") && Boolean.parseBoolean(paramsMap.get("interactive"));
        IEntryParam entryParam = new ParamsConfig(paramsMap);
        CommonParams commonParams = single ? new CommonParams(paramsMap) : new CommonParams(entryParam);
        QSuitsEntry qSuitsEntry = new QSuitsEntry(entryParam, commonParams);
        ILineProcess<Map<String, String>> processor = single || interactive ? qSuitsEntry.whichNextProcessor(true) :
                qSuitsEntry.getProcessor();
        if (process_verify && processor != null) {
            String process = processor.getProcessName();
            if (processor.getNextProcessor() != null) process = processor.getNextProcessor().getProcessName();
            if (ProcessUtils.isDangerous(process)) {
                System.out.println("your last process is " + process + ", are you sure? (y/n): ");
                Scanner scanner = new Scanner(System.in);
                String an = scanner.next();
                if (!an.equalsIgnoreCase("y") && !an.equalsIgnoreCase("yes")) {
                    return;
                }
            }
        }
        if (single) {
            if (processor != null) {
                System.out.println(processor.processLine(commonParams.getMapLine()));
            }
        } else if (interactive) {
            InputSource inputSource = qSuitsEntry.getInputSource();
            inputSource.export(System.in, processor);
        } else {
            IDataSource dataSource = qSuitsEntry.getDataSource();
            if (dataSource != null) {
                dataSource.setProcessor(processor);
                dataSource.export();
            }
        }
        if (processor != null) processor.closeResource();
    }

    public static Map<String, String> getEntryParams(String[] args, Map<String, String> preSetMap) throws IOException {
        if (args != null && args.length > 0) {
            Map<String, String> paramsMap = ParamsUtils.toParamsMap(args, preSetMap);
            if (paramsMap.containsKey("config")) {
                return ParamsUtils.toParamsMap(paramsMap.get("config"));
            } else {
                return paramsMap;
            }
        } else {
            String configFilePath = null;
            List<String> configFiles = new ArrayList<String>(){{
                add("resources" + System.getProperty("file.separator") + "application.config");
                add("resources" + System.getProperty("file.separator") + ".application.config");
                add("resources" + System.getProperty("file.separator") + ".application.properties");
            }};
            for (int i = configFiles.size() - 1; i >= 0; i--) {
                File file = new File(configFiles.get(i));
                if (file.exists()) {
                    configFilePath = configFiles.get(i);
                    System.out.printf("use default config file: %s\n", configFilePath);
                    break;
                }
            }
            if (configFilePath == null) {
                throw new IOException("there is no config file detected.");
            } else if (configFilePath.endsWith(".properties")) {
                return ParamsUtils.toParamsMap(new PropertiesFile(configFilePath).getProperties());
            } else {
                return ParamsUtils.toParamsMap(configFilePath);
            }
        }
    }
}
