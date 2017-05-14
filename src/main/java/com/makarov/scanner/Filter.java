package com.makarov.scanner;

import com.makarov.scanner.filter.ClassFilter;

import java.lang.annotation.Annotation;
import java.util.List;

public interface Filter {

    Filter filterByAnnotation(Class<? extends Annotation>... annotations);

    Filter filterByName(String name);

    Filter filterBySuperClass(Class<?> superClazz);

    Filter filterByCustomFilter(ClassFilter filter);

    List<Class<?>> getClasses();
}
