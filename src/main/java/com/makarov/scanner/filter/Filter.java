package com.makarov.scanner.filter;

import java.lang.annotation.Annotation;
import java.util.List;

public interface Filter {

    @SuppressWarnings("unchecked")
    Filter filterByAnnotation(Class<? extends Annotation>... annotations);

    Filter filterByName(String name);

    Filter filterBySuperClass(Class<?> superClazz);

    Filter filterByCustomFilter(ClassFilter filter);

    List<Class<?>> getClasses();
}
