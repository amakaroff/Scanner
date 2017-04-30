package com.makarov.scanner.util;

public class ScannerStringUtils {

    public static String getCanonicalPackageName(String packageName) {
        return packageName.replace(".", "/") + "/";
    }

    public static String getNormalClassName(String className) {
        return className.replace("/", ".").replace("\\", ".");
    }

    public static String getClassName(String fileName, String packageName) {
        String className = fileName.substring(fileName.indexOf(reverseSlash(packageName)));
        return getNormalClassName(removeClassExpansion(className));
    }

    private static String reverseSlash(String line) {
        return line.replace("/", "\\");
    }

    public static String removeClassExpansion(String className) {
        return className.substring(0, className.lastIndexOf(".class"));
    }

    public static String getJarName(String fileName) {
        return fileName.substring(5, fileName.indexOf("!"));
    }

    public static boolean isClass(String fileName) {
        return fileName.contains(".class");
    }

    public static boolean isJar(String fileName) {
        return fileName.contains("jar");
    }
}
