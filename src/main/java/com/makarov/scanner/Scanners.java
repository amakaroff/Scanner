package com.makarov.scanner;

import com.makarov.scanner.type.FullClassPathScanner;
import com.makarov.scanner.util.FileStringUtils;

import java.util.ArrayList;
import java.util.List;

public class Scanners {

    private FullClassPathScanner scanner;

    Scanners() {
        this.scanner = new FullClassPathScanner();
    }

    List<String> getClassNameList() {
        List<String> classNames = new ArrayList<>();

        String[] classPath = FileStringUtils.getClassPathElements();
        for (String fileName : classPath) {
            classNames.addAll(scanner.scan(fileName));
        }

        return classNames;
    }

    List<String> getFullProjectClassNameList() {
        List<String> classNames = new ArrayList<>();

        String[] classPath = FileStringUtils.getClassPathElements();
        for (String fileName : classPath) {
            if (!FileStringUtils.isSystemJar(fileName)) {
                classNames.addAll(scanner.scan(fileName));
            }
        }

        return classNames;
    }

    List<String> getProjectClassNameList() {
        List<String> classNames = new ArrayList<>();

        String[] classPath = FileStringUtils.getClassPathElements();
        for (String fileName : classPath) {
            if (!FileStringUtils.isJar(fileName)) {
                classNames.addAll(scanner.scan(fileName));
            }
        }

        return classNames;
    }




}
