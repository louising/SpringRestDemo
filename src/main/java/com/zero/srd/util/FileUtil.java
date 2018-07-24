package com.zero.srd.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zero.srd.core.ServiceException;
import com.zero.srd.service.DummyServiceImpl;

public class FileUtil {
    static String DIR_CURR    = ".";
    static String DIR_PARENET = "..";
    
    static Log log = LogFactory.getLog(DummyServiceImpl.class);

    public static void deleteFile(List<File> fileList) {
        if (fileList != null && !fileList.isEmpty()) {
            for (File file : fileList) {
                boolean b = file.delete();
                log.info("delete file: " + b);
            }
        }
    }

    public static void deleteFile(File... files) {
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file != null && file.exists()) {
                    String fileName = file.getAbsolutePath();
                    boolean b = file.delete();
                    log.info(fileName + " delete " + b);
                }
            }
        }
    }

    public static void copyFile(File source, File target, List<String> supportedDocTypes) throws ServiceException {
        /*
        String fileName = target.getAbsolutePath();
        
        checkParameterTrue(!BaseUtils.isEmpty(fileName), ERR_FILE_NAME_NOT_EXCEL); //File name is not empty
        checkParameterTrue(!fileName.contains(PARENET_DIR), ERR_FILE_NAME_INVALID); //File name not contains parent path ..
        checkParameterTrue(supportedDocTypes.contains(getExtension(fileName)), ERROR_UPLOAD_DOC_TYPE); //File extension is valid
        
        try (FileInputStream fi = new FileInputStream(source);
        FileOutputStream fo = new FileOutputStream(target);
        FileChannel in = fi.getChannel();
        FileChannel out = fo.getChannel()) {
        
        in.transferTo(0, in.size(), out);
        } catch (IOException e) {
        log.error("{}", e.getMessage());
        throw new ServiceException(ERROR_WRITE_FILE);
        }
        */
        try {
            org.apache.commons.io.FileUtils.copyFile(source, target);
        } catch (IOException e) {
            log.error(e);
            throw new ServiceException("ERR_FAIL");
        }
    }

    //return org.apache.commons.io.FilenameUtils.getExtension(filename);
    public static String getExtension(String fullFileName) {
        String extension = "";
        if (fullFileName != null) {
            int index = fullFileName.lastIndexOf(DIR_CURR);
            if (index > -1) {
                extension = fullFileName.substring(index + 1);
            }
        }

        return extension;
    }

    /** 
    * @param fileName louis_123456_README.txt 
    * @return louis_123456_
    */
    public static String extractFilePrefixName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf(DIR_CURR));
    }

    /**
    * Get the size of all files that file name with the specified prefix 
    * @param files
    * @param prefixNames
    * @return
    */
    public static long getAllFileSize(File[] files, List<String> prefixNames) {
        long fileSize = 0;
        for (File file : files) {
            String fileName = file.getName();
            String filePrefix = extractFilePrefixName(fileName);
            if (prefixNames.contains(filePrefix)) {
                fileSize = fileSize + file.length();
            }
        }
        return fileSize;
    }

    /** Return file name without extension, e.g. "user123456_0001" */
    public static String buildFilePrefixName(String userId, String fileId) {
        return userId + "_" + fileId;
    }

    /** Return List of file name without extension, e.g. List("user123456_0001") */
    public static List<String> buildFilePrefixNames(String userId, List<String> fileIds) {
        List<String> filePrefixNames = new ArrayList<>();
        if (fileIds != null && !fileIds.isEmpty()) {
            for (String fileId : fileIds) {
                filePrefixNames.add(buildFilePrefixName(userId, fileId));
            }
        }
        return filePrefixNames;
    }

}
