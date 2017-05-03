package com.makarov.scanner.filter;

@FunctionalInterface
public interface ClassFilter {

    boolean isFiltered(Class<?> clazz);
}
