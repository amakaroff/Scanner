package com.makarov.scanner.util;


public class StringUtils {

    public static String getCanonicalPackageName(String packageName) {
        String newName = packageName.replace(".", "/");
        return !newName.contains("/") ? newName + "/" : newName;
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
        return className.substring(0, className.lastIndexOf('.'));
    }

    public static String getJarName(String fileName) {
        return fileName.substring(5, fileName.indexOf("!"));
    }

    public static boolean isClass(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).equals("class");
    }

    public static boolean isJar(String fileName) {
        return fileName.contains("jar");
    }
}
