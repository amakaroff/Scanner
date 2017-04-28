package com.makarov.scanner.filter;

@FunctionalInterface
public interface ClassFilter {

    boolean filter(Class<?> clazz);
}
