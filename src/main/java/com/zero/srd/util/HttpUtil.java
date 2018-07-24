package com.zero.srd.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.zero.srd.core.ServiceException;

public class HttpUtil {
    private static Log log = LogFactory.getLog(BaseUtil.class);

    /*public static void main(String[] args) throws Exception {
    }*/
    
    static void test() throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("languageToken", "Zero");

        String url = "", result = "";
        //1) GET
/*        String url = "http://localhost:8080/SpringBootDemo/dummy/sysInfo";
        url += "?name=" + URLEncoder.encode("Zenna李", "UTF-8");
        url += "&age=21";
        String result = sendGet(url, headers);
        System.out.println("### Get Result: " + result);

        //2) POST
        url = "http://localhost:8080/SpringBootDemo/dummy/page/3/2";
        url += "?name=" + URLEncoder.encode("Zenna李", "UTF-8");
        url += "&age=21";
        JSONObject body = new JSONObject();
        body.put("userId", 2);
        body.put("userName", "Alice李");

        result = sendPost(url, headers, body.toString());
        System.out.println("### POST Result: " + result);*/
        
        //3) POST        
        url = "http://localhost:8080/SpringBootDemo/dummy/add";
        url += "?name=" + URLEncoder.encode("Zenna李", "UTF-8");
        url += "&age=21";
        JSONObject body = new JSONObject();
        body.put("userName",  "Bob李");
        body.put("loginName", "boblee");

        result = sendPost(url, headers, body.toString());
        System.out.println("### ADD Result: " + result);
        
        /*
        RestTemplate restTemplate = new RestTemplate();               
        url = "http://localhost:8080/SpringBootDemo/dummy/add";
        url += "?name=Zenna李";
        url += "&age=21";
        JSONObject body = new JSONObject(); //new HashMap<>();
        body.put("userName",  "Bob张");
        body.put("loginName", "boblee");
        
        result = restTemplate.postForObject(url, body, String.class);
        System.out.println("POST: " + result);
        */
    }

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = null;

        RequestAttributes attr = RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            request = ((ServletRequestAttributes) attr).getRequest();
        }
        return request;
    }
    
    public static HttpServletResponse getResponse() {
        HttpServletResponse response = null;

        RequestAttributes attr = RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            response = ((ServletRequestAttributes) attr).getResponse();
        }
        return response;
    }    

    /**
    * Get header or parameter of HTTP request 
    * @return HTTP header or parameter, blank string if the header not exist 
    */
    public static String getHeader(String headerName) {
        String headerValue = "";

        HttpServletRequest request = getRequest();

        if (request != null) {
            headerValue = request.getHeader(headerName);
            if (StringUtils.isEmpty(headerValue))
                headerValue = request.getParameter(headerName);
        }

        if (headerValue == null) {
            log.warn("Failed to get header: " + headerName);
            headerValue = "";
        }
        return headerValue;
    }

    public static String getParameter(String paramName) {
        String paramValue = "";
        HttpServletRequest request = getRequest();

        if (request != null) {
            paramValue = request.getParameter(paramName);
        }
        return paramValue;
    }

    public static String getCookie(String cookieName) {
        String cookieValue = null;

        RequestAttributes attr = RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpServletRequest request = ((ServletRequestAttributes) attr).getRequest();
            if (request != null) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : request.getCookies()) {
                        if (cookieName.equals(cookie.getName())) {
                            cookieValue = cookie.getValue();
                            break;
                        }
                    }
                }
            }
        }
        return cookieValue;
    }

    /**
    * Send request with authentication to access iData
    * @param urlStr "http://localhost:8080/SpringBootDemo/dummy/sysInfo?name=" + URLEncoder.encode("Zenna李", "UTF-8");
    *   ?name=Zenna%E6%9D%8E&age=21 
    * @param headers
    * @return
    */
    public static String sendGet(String urlStr, Map<String, String> headers) throws ServiceException {
        System.out.println();
        log.info("GET " + urlStr + " Header: " + headers);
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            //connection.setUseCaches(false);
            //Set headers
            //connection.setRequestProperty("Content-Type", "application/json");
            if (headers != null && !headers.isEmpty()) {
                for (String key : headers.keySet()) {
                    connection.setRequestProperty(key, headers.get(key));
                }
            }

            connection.connect(); //Open connect

            try (InputStream in = connection.getInputStream(); 
                 BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                //checkFileSize(in.available());
                readInputStreamToString(reader, sb);
            } catch (IOException e) {
                log.error("{}", e);
            }

            connection.disconnect();
        } catch (IOException e) {
            log.error("{}", e);
        }
        return sb.toString();
    }

    /**
    * Send HTTP POST request
    * @param urlPath "http://localhost:8080/SpringBootDemo/dummy/page/3/2?name=" + URLEncoder.encode("Zenna李", "UTF-8");
    * @param headers
    * @param body e.g. {"userId":2,"userName":"Alice李"}
    * @return
    * @throws Exception
    */
    public static String sendPost(String urlStr, Map<String, String> headers, String jsonBody) throws Exception {
        System.out.println();
        log.info("POST " + urlStr + " Body: " + jsonBody + " Header: " + headers);
        String result = "";

        //1) Init HttpURLConnection
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setReadTimeout(0); //infinite

        //2) Set HTTP header
        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8"); //"application/json", application/x-www-form-urlencoded
        conn.setRequestProperty("Connection", "Keep-Alive");
        //conn.setRequestProperty("Charset", "UTF-8");
        if (headers != null && !headers.isEmpty()) {
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }
        }
        conn.connect();

        //3) Write POST body
        //httpConn.getOutputStream() will invoke httpConn.connect();
        //OutputStreamWriter dataOut = new OutputStreamWriter(out)
        //DataOutputStream dataOut = new DataOutputStream(out)
        try (OutputStream out = conn.getOutputStream(); 
             DataOutputStream dataOut = new DataOutputStream(out) ) { //, "UTF-8"
            //dataOut.writeBytes(jsonBody); //DataOutputStream, ERR: can not process GBK
            dataOut.writeChars(jsonBody);   //DataOutputStream
            //dataOut.write(jsonBody);      //OutputStreamWriter
            dataOut.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }

        //4) Get result 
        int responseCode = conn.getResponseCode();
        result = "Response Code: " + responseCode;
        if (HttpURLConnection.HTTP_OK == responseCode) {
            try (InputStream in = conn.getInputStream(); BufferedReader responseReader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
                //checkFileSize(in.available());
                StringBuffer sb = new StringBuffer();
                readInputStreamToString(responseReader, sb);
                result = sb.toString();
            } catch (Exception e) {
                log.error("{}", e);
                result = e.getMessage();
            }
        }

        return result;
    }

    /**<pre>
    * Read InputStream to String
    * This method is used to replace the following code:
    * 
    * String readLine;
    * while ((readLine = responseReader.readLine()) != null) {
    * sb.append(readLine).append("\n");
    * }
    * </pre>
    */
    private static void readInputStreamToString(BufferedReader br, StringBuffer sb) throws IOException {
        char[] c = new char[1024];
        int read = 0;
        while (read != -1) {
            read = br.read(c);
            if (read < 0) {
                break;
            }

            if (read < 1024) {
                char[] tmp = new char[read];
                tmp = Arrays.copyOf(c, read);
                sb.append(tmp);
            } else {
                sb.append(c);
            }
        }
    }

    public static void prtRequest() {
        String SEP = "    ";
        HttpServletRequest request = getRequest();

        String str = "\n### Request Info:\n";
        str += SEP + "Method=" + request.getMethod() + "\n";
        str += SEP + "getRequestedSessionId=" + request.getRequestedSessionId() + "\n";
        str += SEP + "getRemoteAddr=" + request.getRemoteAddr() + "\n";
        str += SEP + "getRemoteHost=" + request.getRemoteHost() + "\n";
        str += SEP + "getRemotePort=" + request.getRemotePort() + "\n";
        str += SEP + "getLocalAddr=" + request.getLocalAddr() + "\n";
        str += SEP + "getLocalPort=" + request.getLocalPort() + "\n";
        
        str += "Coookies:\n";
        String cookieStr = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie k : cookies) {
                cookieStr += SEP + String.format("%s=%s Domain:%s Path:%s", k.getName(), k.getValue(), k.getDomain(), k.getPath()) + "\n";
            }
        if (cookieStr.equals(""))
            cookieStr = SEP + "None" + "\n"; 
        str += cookieStr;

        String paramStr = "";
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            paramStr += SEP + paramName + "=" + request.getParameter(paramName) + "\n";
        }
        if (paramStr.equals(""))
            paramStr = SEP + "None" + "\n";
        str += "Parameters:\n" + paramStr;

        String headerStr = "";
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            headerStr += SEP + header + "=" + request.getHeader(header) + "\n";
        }
        if (headerStr.equals(""))
            headerStr = SEP + "None";
        else
            headerStr = headerStr.substring(0, headerStr.length() - 1);
        str += "Headers:\n" + headerStr;

        log.info(str);
    }
}
