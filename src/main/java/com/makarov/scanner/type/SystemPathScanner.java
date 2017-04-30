package com.makarov.scanner.type;

import com.makarov.scanner.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SystemPathScanner {

    private String packageName;

    public SystemPathScanner(String packageName) {
        this.packageName = packageName;
    }

    public List<String> scan(String systemPath) {
        File folder = new File(systemPath);

        List<String> classNames = new ArrayList<String>();
        try {
            classNames = folderScanner(folder.getPath());
        } catch (IOException exception) {
            //Logging
        }

        return classNames;
    }

    private List<String> getEntityFromJarFile(File jarFile) throws IOException {
        List<String> classNames = new ArrayList<String>();
        Enumeration<JarEntry> jarEntries = new JarFile(jarFile.getAbsoluteFile()).entries();

        while (jarEntries.hasMoreElements()) {
            String entryName = jarEntries.nextElement().getName();
            if (entryName.contains(packageName) && StringUtils.isClass(entryName)) {
                entryName = StringUtils.getNormalClassName(entryName);
                classNames.add(StringUtils.removeClassExpansion(entryName));
            }
        }

        return classNames;
    }

    private List<String> folderScanner(String folderPath) throws IOException {
        List<String> classNames = new ArrayList<String>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.isFile()) {
                    classNames.addAll(folderScanner(file.getPath()));
                } else if (StringUtils.isJar(file.getName())) {
                    classNames.addAll(getEntityFromJarFile(file));
                }
            }
        }

        return classNames;
    }
}
