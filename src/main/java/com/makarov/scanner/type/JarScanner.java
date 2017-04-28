package com.makarov.scanner.type;

import com.makarov.scanner.util.StringUtils;

import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarScanner {

    private String packageName;

    public JarScanner(String packageName) {
        this.packageName = packageName;
    }

    public List<String> scan(URL packageURL) {
        List<String> classNames = new ArrayList<String>();

        try {
            Enumeration<JarEntry> jarEntries = unzipJar(packageURL, "UTF-8");
            classNames.addAll(getEntityFromJarFile(jarEntries));
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }

        return classNames;
    }

    private Enumeration<JarEntry> unzipJar(URL packageURL, String coding) throws Exception{
        String jarFileName = StringUtils.getJarName(URLDecoder.decode(packageURL.getFile(), coding));
        JarFile jarFile = new JarFile(jarFileName);
        return jarFile.entries();
    }

    public List<String> getEntityFromJarFile(Enumeration<JarEntry> jarEntries) throws Exception {
        List<String> classNames = new ArrayList<String>();

        String entryName;
        while (jarEntries.hasMoreElements()) {
            entryName = jarEntries.nextElement().getName();
            if (entryName.startsWith(packageName) && entryName.length() > packageName.length() + 5) {
                if (StringUtils.isClass(entryName)) {
                    entryName = StringUtils.getNormalClassName(entryName);
                    System.out.println(StringUtils.removeClassExpansion(entryName));
                    classNames.add(StringUtils.removeClassExpansion(entryName));
                }
            }
        }

        return classNames;
    }
}
