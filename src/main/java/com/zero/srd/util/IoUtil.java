package com.zero.srd.util;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class IoUtil {
    static Log log = LogFactory.getLog(IoUtil.class);

    private static byte[] getBytes(InputStream is) throws IOException {
        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] mBytes = new byte[size];
            while ((len = is.read(mBytes, 0, size)) != -1)
                bos.write(mBytes, 0, len);

            buf = bos.toByteArray();
        }
        return buf;
    }

    public static void writeInputStream2Response(InputStream is, String fileName) throws IOException {
        byte[] bytes = getBytes(is);

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        try (OutputStream toClient = new BufferedOutputStream(response.getOutputStream())) {
            response.reset();
            //response.setHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "iso8859-1"));

            response.setContentType("application/octet-stream");
            toClient.write(bytes);
            toClient.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void writeFile2Response(File file) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            String fileName = file.getName();
            writeInputStream2Response(is, fileName);
        } catch (IOException e) {
            throw e;
        }
    }
}
