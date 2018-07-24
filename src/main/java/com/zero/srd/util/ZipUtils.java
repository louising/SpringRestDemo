package com.zero.srd.util;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.zero.srd.core.ServiceException;

/**
 * Usage
 <pre>
    compress(new File("C:/temp/BIA评估工具_WBS计划.mpp"), new File("c:/temp/bia-wbs.zip")); 
    
    File file = new File("c:/temp/sso.zip");
    if (file.exists())
    file.delete();
    compress(new File("C:/temp/SSO"), file);
    
    file = new File("c:/temp/abc.zip");
    if (file.exists())
    file.delete();
    compress(new File("C:/temp/ZeroFramework"), file);
 </pre>
 * @author Louisling
 * @version 2018-07-17
 */
public class ZipUtils {
    static String EXTENSION_ZIP         = ".zip";
    
    private ZipUtils() {
    }

    /**
    * Zip a dir or file to zip.
    * 
    * @param srcFile dir or a file, if is dir, will find files in recursion, zip file will not contains directory 
    * @param zipFile 
    */
    public static void compress(File srcFile, File zipFile) throws IOException {
        OutputStream outputStream = Files.newOutputStream(zipFile.toPath(), CREATE, WRITE); //OutputStream outputStream = new FileOutputStream(zipFile);

        try (ZipOutputStream out = new ZipOutputStream(outputStream)) {
            doCompress(srcFile, out, "");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
    * Zip multiple files
    * 
    * @param files File instead of directory
    * @param zipFile
    * @throws Exception
    */
    public static void compressFiles(File[] files, File zipFile) throws Exception {
        if (files == null || files.length == 0) {
            return;
        }

        OutputStream outputStream = Files.newOutputStream(zipFile.toPath(), CREATE, WRITE); //new FileOutputStream(FileUtils.getFile(zipFile))
        try (ZipOutputStream out = new ZipOutputStream(outputStream)) {
            for (File file : files) {
                doZip(file, out, "");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
    * 压缩目录或者单个文件到指定的压缩文件，可以递归查询文件夹下面的文件
    * 
    * @param inFile
    * @param out
    * @param dir 指定当前文件压缩到哪个子文件夹
    * @throws IOException
    */
    private static void doCompress(File inFile, ZipOutputStream out, String dir) throws IOException {
        if (inFile.isDirectory()) {
            File[] files = inFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    String name = inFile.getName();
                    if (!"".equals(dir)) {
                        name = dir + File.separator + name;
                    }

                    doCompress(file, out, name);
                }
            }
        } else {
            doZip(inFile, out, dir);
        }
    }

    /**
    * 压缩单个文件到指定的压缩文件
    * 
    * @param inFile
    * @param out
    * @param dir
    * @throws IOException
    */
    private static void doZip(File inFile, ZipOutputStream out, String dir) throws IOException {
        String entryName = null;
        //entryName = inFile.getName();
        if (!"".equals(dir)) {
            entryName = dir + "/" + inFile.getName();
        } else {
            entryName = inFile.getName();
        }

        if (dir.indexOf(File.separator) < 0) {
            entryName = inFile.getName();
        } else {
            int index = entryName.indexOf(File.separator);
            entryName = entryName.substring(index + 1);
        }

        ZipEntry entry = new ZipEntry(entryName);
        out.putNextEntry(entry);

        int len = 0;
        byte[] buffer = new byte[1024];

        try (FileInputStream fis = new FileInputStream(inFile)) {
            while ((len = fis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
                out.flush();
            }
            out.closeEntry();
        }
    }

    /**
    * Write files to zip file
    * @param tmpDirStr e.g. "c:/tmp/"
    * @param zipName
    * @throws ServiceException
    */
    public static void writeFileToZip(String tmpDirStr, String zipName) throws ServiceException {
        File tmpDir = new File(tmpDirStr);
        File[] files = tmpDir.listFiles();
        File zipFile = new File(tmpDirStr + zipName + EXTENSION_ZIP);
        try {
            compressFiles(files, zipFile);
        } catch (Exception e) {
            FileUtil.deleteFile(zipFile);
            FileUtil.deleteFile(tmpDir);
            throw new ServiceException("ERR_ZIP_DOC");
        }
    }
}
