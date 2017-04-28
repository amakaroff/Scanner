package com.makarov.scanner.util;

import java.lang.annotation.Annotation;


public class FilterUtils {

    public static boolean annotationFilter(Class<? extends Annotation>[] annotations, Class<?> clazz) {
        for (Class<? extends Annotation> annotation : annotations) {
            if (clazz.isAnnotationPresent(annotation)) {
                return true;
            }
        }

        return false;
    }

    public static boolean classNameFilter(String className, Class<?> clazz) {
        return clazz.getName().contains(className);
    }

    public static boolean classFilter(Class<?> superClass, Class<?> clazz) {
        return  (clazz.isAssignableFrom(superClass));
    }
}
