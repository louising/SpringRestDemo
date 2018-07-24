package com.zero.srd.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {
    
    /*
     * Access-Control-Expose-Headers
     *     response header indicates which headers can be exposed as part of the response by listing their names
     *     By default, only the 6 simple response headers are exposed:
     *         Cache-Control, Content-Language, Content-Type, Expires, Last-Modified, Pragma
     *     If you want clients to be able to access other headers, you have to list them using the Access-Control-Expose-Headers header
     *         
     * Access-Control-Allow-Headers(Optional)
     *     This header is required if the request has an Access-Control-Request-Headers header.
     *     
     *     The simple headers, Accept, Accept-Language, Content-Language, Content-Type (application/x-www-form-urlencoded, multipart/form-data, or text/plain), 
     *     are always available and don't need to be listed by this header.
     *     
     *     response header is used in response to a preflight request to indicate which HTTP headers can be used during the actual request
     *     preflight request:
     *         A CORS preflight request is a CORS request that checks to see if the CORS protocol is understood.
     *         It is an OPTIONS request, using three HTTP request headers: Access-Control-Request-Method, Access-Control-Request-Headers, and the Origin header.
     *         A preflight request is automatically issued by a browser, when needed. In normal cases, front-end developers don't need to craft such requests themselves.
     *         
     * Access-Control-Allow-Credentials(Optional)
     *     If allowed to send cookie, default is false
     * 
     * Access-Control-Max-Age
     *     How many seconds will be cached for the response result of preflight request for cross domain access(that send a OPTIONS request), -1 forbidden
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setContentType("text/html;charset=UTF-8");
        
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        res.setHeader("XDomainRequestAllowed", "1");

        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");        
        res.setHeader("Access-Control-Allow-Credentials", "true"); //if cookie is allowed
        res.setHeader("Access-Control-Max-Age", "0");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}