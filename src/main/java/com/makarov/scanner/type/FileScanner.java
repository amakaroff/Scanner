package com.makarov.scanner.type;


import com.makarov.scanner.util.FileStringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileScanner {

    private List<String> filePaths;
    private String fileName;
    private String fileExtension;

    public FileScanner(String fileName, String fileExtension) {
        this.filePaths = new ArrayList<>();
        this.fileName = fileName;
        this.fileExtension = fileExtension;
    }

    public List<String> getFilePaths() {
        String projectFolderName = getProjectFolder();
        getFileNames(projectFolderName);
        return filePaths;
    }

    private void getFileNames(String folderName) {
        File folder = new File(folderName);
        File[] content = folder.listFiles();

        if (content != null) {
            for (File file : content) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    if (FileStringUtils.getFileExtension(fileName).equalsIgnoreCase(fileExtension)
                            && FileStringUtils.getFileName(fileName).contains(this.fileName)) {
                        filePaths.add(file.getPath());
                    }
                } else {
                    getFileNames(file.getPath());
                }
            }
        }
    }

    private String getProjectFolder() {
        String[] classPath = System.getProperty("java.class.path").split(";");
        String defaultFolder = "";
        for (String fileName : classPath) {
            if (!FileStringUtils.isJar(fileName)) {
                defaultFolder = fileName;
            }
        }

        return defaultFolder;
    }
}
