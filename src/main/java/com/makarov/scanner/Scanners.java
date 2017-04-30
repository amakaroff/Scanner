package com.makarov.scanner;

import com.makarov.scanner.type.JarScanner;
import com.makarov.scanner.type.SystemPathScanner;
import com.makarov.scanner.type.TargetScanner;
import com.makarov.scanner.util.StringUtils;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Scanners {

    private static final String JAVA_HOME = System.getenv("JAVA_HOME") + "\\jre\\lib\\";

    private static final String MAVEN_HOME = System.getProperty("user.home") + "\\.m2";

    private static JarScanner jarScanner;

    private static SystemPathScanner systemPathScanner;

    private static TargetScanner targetScanner;

    Scanners(String packageName) {
        jarScanner = new JarScanner(packageName);
        targetScanner = new TargetScanner(packageName);
        systemPathScanner = new SystemPathScanner(packageName);
    }

    List<String> systemScan() {
        List<String> classNames = systemPathScanner.scan(JAVA_HOME);

        if (classNames.isEmpty()) {
            classNames = systemPathScanner.scan(MAVEN_HOME);
        }

        return classNames;
    }

    List<String> localScan(URL packageURL) {
        List<String> classNames = new ArrayList<String>();

        if (StringUtils.isJar(packageURL.getProtocol())) {
            classNames = jarScanner.scan(packageURL);
        } else {
            try {
                URI packageURI = new URI(packageURL.toString());
                classNames = targetScanner.scan(packageURI);
            } catch (Exception exception) {
                //Logging
            }
        }
        return classNames;
    }
}
