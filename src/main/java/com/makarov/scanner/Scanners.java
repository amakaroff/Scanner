package com.makarov.scanner;

import com.makarov.scanner.type.FullClassPathScanner;

import java.util.ArrayList;
import java.util.List;

public class Scanners {

    private FullClassPathScanner scanner;

    Scanners() {
        this.scanner = new FullClassPathScanner();
    }

    List<String> getClassNameList() {
        List<String> classNames = new ArrayList<>();

        String[] classPath = System.getProperty("java.class.path").split(";");
        for (String fileName : classPath) {
            classNames.addAll(scanner.scan(fileName));
        }

        return classNames;
    }

    List<String> getFullProjectClassNameList() {
        List<String> classNames = new ArrayList<>();

        String[] classPath = System.getProperty("java.class.path").split(";");
        for (String fileName : classPath) {
            String fileSimpleName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
            if (!systemJars.contains(fileSimpleName)) {
                classNames.addAll(scanner.scan(fileName));
            }
        }

        return classNames;
    }

    List<String> getProjectClassNameList() {
        List<String> classNames = new ArrayList<>();

        String[] classPath = System.getProperty("java.class.path").split(";");
        for (String fileName : classPath) {
            if (!fileName.contains(".jar")) {
                classNames.addAll(scanner.scan(fileName));
            }
        }

        return classNames;
    }

    private List<String> systemJars = new ArrayList<String>() {
        {
            add("charsets.jar");
            add("deploy.jar");
            add("javaws.jar");
            add("jce.jar");
            add("jfr.jar");
            add("jfxswt.jar");
            add("jsse.jar");
            add("management-agent.jar");
            add("plugin.jar");
            add("resources.jar");
            add("rt.jar");
            add("access-bridge-64.jar");
            add("cldrdata.jar");
            add("dnsns.jar");
            add("jaccess.jar");
            add("jfxrt.jar");
            add("localedata.jar");
            add("nashorn.jar");
            add("sunec.jar");
            add("sunjce_provider.jar");
            add("sunmscapi.jar");
            add("sunpkcs11.jar");
            add("zipfs.jar");
            add("idea_rt.jar");
        }
    };
}
