package com.makarov.scanner;


import com.makarov.scanner.type.FileScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilePathScanner {

    public static List<File> findFiles(String fileName, String fileExtension) {
        List<File> files = new ArrayList<>();

        if (fileName == null) {
            fileName = "";
        }

        FileScanner scanner = new FileScanner(fileName, fileExtension);
        for (String filePaths : scanner.getFilePaths()) {
            files.add(new File(filePaths));
        }

        return files;
    }
}
