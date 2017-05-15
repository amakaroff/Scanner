package com.makarov.scanner;

import com.makarov.scanner.filter.ClassFilter;
import com.makarov.scanner.filter.Filter;
import com.makarov.scanner.util.FilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class ClassPathScanner {

    private static final Logger logger = LoggerFactory.getLogger(ClassPathScanner.class);

    private static Scanners scanners = new Scanners();

    public static Filter fullScan() {
        List<String> classNames = scanners.getClassNameList();
        return new ClassPathScanner().new InnerFilter(transform(classNames));
    }

    public static Filter fullScan(String packageName) {
        List<String> classNames = new ArrayList<>();
        for (String className : scanners.getClassNameList()) {
            if (className.contains(packageName)) {
                classNames.add(className);
            }
        }

        return new ClassPathScanner().new InnerFilter(transform(classNames));
    }

    public static Filter fullProjectScan() {
        List<String> classNames = scanners.getFullProjectClassNameList();
        return new ClassPathScanner().new InnerFilter(transform(classNames));
    }

    public static Filter fullProjectScan(String packageName) {
        List<String> classNames = new ArrayList<>();
        for (String className : scanners.getProjectClassNameList()) {
            if (className.contains(packageName)) {
                classNames.add(className);
            }
        }

        return new ClassPathScanner().new InnerFilter(transform(classNames));
    }

    public static Filter projectScan() {
        List<String> classNames = scanners.getProjectClassNameList();
        return new ClassPathScanner().new InnerFilter(transform(classNames));
    }

    private static List<Class<?>> transform(List<String> classNames) {
        List<Class<?>> classes = new ArrayList<>();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        for (String className : classNames) {
            try {
                classes.add(contextClassLoader.loadClass(className));
            } catch (Throwable exception) {
                if (logger.isDebugEnabled()) {
                    logger.error("Class not found: ", exception);
                }
            }
        }

        return classes;
    }

    private class InnerFilter implements Filter {

        private List<Class<?>> classes;

        private InnerFilter(List<Class<?>> classes) {
            this.classes = classes;
        }

        @SafeVarargs
        public final Filter filterByAnnotation(Class<? extends Annotation>... annotations) {
            List<Class<?>> filteredClasses = new ArrayList<>();
            for (Class<?> clazz : classes) {
                if (FilterUtils.isAnnotationsPresent(annotations, clazz)) {
                    filteredClasses.add(clazz);
                }
            }

            return new InnerFilter(filteredClasses);
        }

        public Filter filterByName(String name) {
            List<Class<?>> filteredClasses = new ArrayList<>();
            for (Class<?> clazz : classes) {
                if (FilterUtils.isClassNameContains(name, clazz)) {
                    filteredClasses.add(clazz);
                }
            }

            return new InnerFilter(filteredClasses);
        }

        public Filter filterBySuperClass(Class<?> superClazz) {
            List<Class<?>> filteredClasses = new ArrayList<>();
            for (Class<?> clazz : classes) {
                if (FilterUtils.isAssignableFrom(superClazz, clazz)) {
                    filteredClasses.add(clazz);
                }
            }

            return new InnerFilter(filteredClasses);
        }

        public Filter filterByCustomFilter(ClassFilter classFilter) {
            List<Class<?>> filteredClasses = new ArrayList<>();
            for (Class<?> clazz : classes) {
                if (classFilter.isFiltered(clazz)) {
                    filteredClasses.add(clazz);
                }
            }

            return new InnerFilter(filteredClasses);
        }

        public List<Class<?>> getClasses() {
            return classes;
        }
    }
}
