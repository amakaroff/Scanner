package com.makarov.scanner;

import com.makarov.scanner.filter.ClassFilter;
import com.makarov.scanner.type.FullClassPathScanner;
import com.makarov.scanner.util.FilterUtils;
import com.makarov.scanner.util.ScannerStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassPathScanner {

    private static final Logger logger = LoggerFactory.getLogger(ClassPathScanner.class);

    public static Scanner packageScan(String packageName) {
        String canonicalPackageName = ScannerStringUtils.getCanonicalPackageName(packageName);
        Scanners scanners = new Scanners(canonicalPackageName);

        List<String> classNames;
        URL packageURL = Thread.currentThread().getContextClassLoader().getResource(packageName);

        if (packageURL == null) {
            classNames = scanners.systemScan();
        } else {
            classNames = scanners.localScan(packageURL);
        }

        return new ClassPathScanner().new InnerScanner(transform(classNames));
    }

    public static Scanner classPathScan() {
        List<String> classNames = new ArrayList<>();
        String[] classPath = System.getProperty("java.class.path").split(";");
        FullClassPathScanner fullScanner = new FullClassPathScanner();
        for (String fileName : classPath) {
            classNames.addAll(fullScanner.scan(fileName));
        }

        return new ClassPathScanner().new InnerScanner(transform(classNames));
    }

    private static List<Class<?>> transform(List<String> classNames) {
        List<Class<?>> classes = new ArrayList<>();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        for (String className : classNames) {
            try {
                classes.add(contextClassLoader.loadClass(className));
            } catch (Throwable exception) {
                logger.error("Class not found: ", exception);
            }
        }

        return classes;
    }

    private class InnerScanner implements Scanner {

        private List<Class<?>> classes;

        private InnerScanner(List<Class<?>> classes) {
            this.classes = classes;
        }

        @SafeVarargs
        public final Scanner filterByAnnotation(Class<? extends Annotation>... annotations) {
            for (Class<?> clazz : classes) {
                if (!FilterUtils.isAnnotationsPresent(annotations, clazz)) {
                    classes.remove(clazz);
                }
            }

            return this;
        }

        public Scanner filterByName(String name) {
            List<Class<?>> filteredClasses = new ArrayList<>();
            for (Class<?> clazz : classes) {
                if (FilterUtils.isClassNameContains(name, clazz)) {
                    filteredClasses.add(clazz);
                }
            }

            return new InnerScanner(filteredClasses);
        }

        public Scanner filterBySuperClass(Class<?> superClazz) {
            List<Class<?>> filteredClasses = new ArrayList<>();
            for (Class<?> clazz : classes) {
                if (FilterUtils.isAssignableFrom(superClazz, clazz)) {
                    filteredClasses.add(clazz);
                }
            }

            return new InnerScanner(filteredClasses);
        }

        public Scanner filterByCustomFilter(ClassFilter classFilter) {
            List<Class<?>> filteredClasses = new ArrayList<>();
            for (Class<?> clazz : classes) {
                if (classFilter.isFiltered(clazz)) {
                    filteredClasses.add(clazz);
                }
            }

            return new InnerScanner(filteredClasses);
        }


        public List<Class<?>> getClasses() {
            return classes;
        }
    }
}
