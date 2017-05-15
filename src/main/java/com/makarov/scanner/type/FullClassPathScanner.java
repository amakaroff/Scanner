package com.makarov.scanner.type;

import com.makarov.scanner.util.FileStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FullClassPathScanner {

    private static final Logger logger = LoggerFactory.getLogger(FullClassPathScanner.class);

    private String classFolder;

    private List<String> classNames;

    public List<String> scan(String filePath) {
        try {
            if (FileStringUtils.isJar(filePath)) {
                JarFile jarFile = new JarFile(filePath);
                return getClassNames(jarFile.entries());
            } else {
                classFolder = filePath;
                classNames = new ArrayList<>();
                return getClassNames(filePath);
            }
        } catch (Exception exception) {
            if (logger.isDebugEnabled()) {
                logger.error("Can't read file: {}", filePath, exception);
            }
        }

        return new ArrayList<>();
    }

    private List<String> getClassNames(Enumeration<JarEntry> entities) {
        List<String> classNames = new ArrayList<>();

        while (entities.hasMoreElements()) {
            String entityName = entities.nextElement().toString();
            if (FileStringUtils.isClass(entityName)) {
                classNames.add(FileStringUtils.normalizeClassName(entityName));
            }
        }

        return classNames;
    }

    private List<String> getClassNames(String classPath) {
        File folder = new File(classPath);
        File[] content = folder.listFiles();

        if (content != null) {
            for (File file : content) {
                if (file.isFile() && FileStringUtils.isClass(file.getName())) {
                    String fileName = file.getPath().replace(classFolder, "").substring(1);
                    classNames.add(FileStringUtils.normalizeClassName(fileName));
                } else {
                    getClassNames(file.getPath());
                }
            }
        }

        return classNames;
    }
}
