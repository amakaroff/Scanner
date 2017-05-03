package com.makarov.scanner.type;

import com.makarov.scanner.util.ScannerStringUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class TargetScanner {

    private String packageName;

    private List<String> classNames;

    public TargetScanner(String packageName) {
        this.packageName = packageName;
        classNames = new ArrayList<>();
    }

    public List<String> scan(URI packageURI) throws URISyntaxException, MalformedURLException {
        String path = packageURI.getPath();

        File folder = new File(path);
        File[] content = folder.listFiles();

        if (content != null) {
            for (File file : content) {
                if (file.isFile()) {
                    classNames.add(ScannerStringUtils.getClassName(file.getPath(), packageName));
                } else {
                    scan(file.toURI());
                }
            }
        }

        return classNames;
    }
}
