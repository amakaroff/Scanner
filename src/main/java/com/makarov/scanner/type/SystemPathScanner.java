package com.makarov.scanner.type;

import com.makarov.scanner.util.ScannerStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SystemPathScanner {

    private static final Logger logger = LoggerFactory.getLogger(FullClassPathScanner.class);

    private String packageName;

    public SystemPathScanner(String packageName) {
        this.packageName = packageName;
    }

    public List<String> scan(String systemPath) {
        File folder = new File(systemPath);

        return folderScanner(folder.getPath());
    }

    private List<String> getEntityFromJarFile(File jarFile) {
        List<String> classNames = new ArrayList<>();
        Enumeration<JarEntry> jarEntries;
        try {
            jarEntries = new JarFile(jarFile.getAbsoluteFile()).entries();
        } catch (IOException exception) {
            logger.error("Can't scan jar: {}", jarFile.getName(), exception);
            return new ArrayList<>();
        }

        while (jarEntries.hasMoreElements()) {
            String entryName = jarEntries.nextElement().getName();
            if (entryName.contains(packageName) && ScannerStringUtils.isClass(entryName)) {
                entryName = ScannerStringUtils.getNormalClassName(entryName);
                classNames.add(ScannerStringUtils.removeClassExpansion(entryName));
            }
        }

        return classNames;
    }

    private List<String> folderScanner(String folderPath) {
        List<String> classNames = new ArrayList<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.isFile()) {
                    classNames.addAll(folderScanner(file.getPath()));
                } else if (ScannerStringUtils.isJar(file.getName())) {
                    classNames.addAll(getEntityFromJarFile(file));
                }
            }
        }

        return classNames;
    }
}
