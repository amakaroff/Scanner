package com.makarov.scanner.type;

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
            if (isJar(filePath)) {
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
            if (isClass(entityName)) {
                classNames.add(normalizeClassName(entityName));
            }
        }

        return classNames;
    }

    private List<String> getClassNames(String classPath) {
        File folder = new File(classPath);
        File[] content = folder.listFiles();

        if (content != null) {
            for (File file : content) {
                if (file.isFile() && isClass(file.getName())) {
                    String fileName = file.getPath().replace(classFolder, "").substring(1);
                    classNames.add(normalizeClassName(fileName));
                } else {
                    getClassNames(file.getPath());
                }
            }
        }

        return classNames;
    }

    private boolean isJar(String fileName) {
        return fileName.contains(".jar");
    }

    private boolean isClass(String fileName) {
        return fileName.contains(".class");
    }

    private String normalizeClassName(String className) {
        String newClassName = className.replace("/", ".").replace("\\", ".");
        return newClassName.substring(0, newClassName.lastIndexOf(".class"));
    }
}
