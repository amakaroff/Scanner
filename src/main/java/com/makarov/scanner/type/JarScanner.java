package com.makarov.scanner.type;

import com.makarov.scanner.util.ScannerStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarScanner {

    private static final Logger logger = LoggerFactory.getLogger(JarScanner.class);

    private String packageName;

    public JarScanner(String packageName) {
        this.packageName = packageName;
    }

    public List<String> scan(URL packageURL) {
        List<String> classNames = new ArrayList<>();

        try {
            Enumeration<JarEntry> jarEntries = unzipJar(packageURL, "UTF-8");
            classNames.addAll(getEntityFromJarFile(jarEntries));
        } catch (Exception exception) {
            logger.error("Can't scan jar file: {}", packageURL.getFile(), exception);
        }

        return classNames;
    }

    private Enumeration<JarEntry> unzipJar(URL packageURL, String coding) throws Exception {
        String jarFileName = ScannerStringUtils.getJarName(URLDecoder.decode(packageURL.getFile(), coding));
        JarFile jarFile = new JarFile(jarFileName);
        return jarFile.entries();
    }

    private List<String> getEntityFromJarFile(Enumeration<JarEntry> jarEntries) throws Exception {
        List<String> classNames = new ArrayList<>();

        String entryName;
        while (jarEntries.hasMoreElements()) {
            entryName = jarEntries.nextElement().getName();
            System.out.println(entryName);
            if (entryName.startsWith(packageName) && entryName.length() > packageName.length() + 5) {
                if (ScannerStringUtils.isClass(entryName)) {
                    entryName = ScannerStringUtils.getNormalClassName(entryName);
                    classNames.add(ScannerStringUtils.removeClassExpansion(entryName));
                }
            }
        }

        return classNames;
    }
}
