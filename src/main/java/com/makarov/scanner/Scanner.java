package com.makarov.scanner;


import com.makarov.scanner.filter.ClassFilter;

import java.lang.annotation.Annotation;
import java.util.List;

public interface Scanner {

    Scanner annotationFilter(Class<? extends Annotation>... annotations);

    Scanner filterByName(String name);

    Scanner filterBySuperClass(Class<?> superClazz);

    Scanner filter(ClassFilter filter);

    List<Class<?>> getClasses();
}
