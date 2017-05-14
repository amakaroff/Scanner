package com.makarov.scanner.type;


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
                    if (getFileExtension(fileName).equals(fileExtension)
                            && getFileName(fileName).contains(this.fileName)) {
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
            if (!getFileExtension(fileName).equals("jar")) {
                defaultFolder = fileName;
            }
        }

        return defaultFolder;
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String getFileName(String fileFullName) {
        return fileFullName.substring(fileFullName.lastIndexOf("/") + 1, fileFullName.lastIndexOf("."));
    }
}
