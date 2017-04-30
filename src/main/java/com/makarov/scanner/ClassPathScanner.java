package com.makarov.scanner;

import com.makarov.scanner.filter.ClassFilter;
import com.makarov.scanner.util.FilterUtils;
import com.makarov.scanner.util.StringUtils;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ClassPathScanner {

    private static Scanners scanners;

    public static Scanner packageScan(String packageName) {
        String canonicalPackageName = StringUtils.getCanonicalPackageName(packageName);
        scanners = new Scanners(canonicalPackageName);

        List<String> classNames;

        URL packageURL = Thread.currentThread().getContextClassLoader().getResource(packageName);

        if (packageURL == null) {
            classNames = scanners.systemScan();
        } else {
            classNames = scanners.localScan(packageURL);
        }

        return new ClassPathScanner().new Scanner(transform(classNames));
    }

    private static List<Class<?>> transform(List<String> classNames) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        for (String className : classNames) {
            try {
                classes.add(contextClassLoader.loadClass(className));
            } catch (Throwable exception) {
                //Logging
            }
        }

        return classes;
    }

    public class Scanner {

        private List<Class<?>> classes;

        private Scanner(List<Class<?>> classes) {
            this.classes = classes;
        }

        public Scanner annotationFilter(Class<? extends Annotation>... annotations) {
            List<Class<?>> classes = new ArrayList<Class<?>>();

            for (Class<?> clazz : this.classes) {
                if (FilterUtils.annotationFilter(annotations, clazz)) {
                    classes.add(clazz);
                }
            }

            return new Scanner(classes);
        }

        public Scanner filterByName(String name) {
            List<Class<?>> classes = new ArrayList<Class<?>>();

            for (Class<?> clazz : this.classes) {
                if (FilterUtils.classNameFilter(name, clazz)) {
                    classes.add(clazz);
                }
            }

            return new Scanner(classes);
        }

        public Scanner filterBySuperClass(Class<?> superClazz) {
            List<Class<?>> classes = new ArrayList<Class<?>>();

            for (Class<?> clazz : this.classes) {
                if (FilterUtils.classFilter(superClazz, clazz)) {
                    classes.add(clazz);
                }
            }

            return new Scanner(classes);
        }

        public Scanner filter(ClassFilter filter) {
            List<Class<?>> classes = new ArrayList<Class<?>>();

            for (Class<?> clazz : this.classes) {
                if (filter.filter(clazz)) {
                    classes.add(clazz);
                }
            }

            return new Scanner(classes);
        }


        public List<Class<?>> getClasses() {
            return classes;
        }
    }
}
