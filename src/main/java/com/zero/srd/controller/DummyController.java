package com.zero.srd.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zero.srd.core.AppCallable;
import com.zero.srd.core.AppRunnable;
import com.zero.srd.core.BaseController;
import com.zero.srd.core.PageVO;
import com.zero.srd.core.ResponseVO;
import com.zero.srd.core.ServiceException;
import com.zero.srd.service.DummyService;
import com.zero.srd.vo.DummyVO;

/*
  POST http://localhost:8080/SpringRestDemo/dummy/addUser BODY { userId: 101, userName: "Alice"}
DELETE http://localhost:8080/SpringRestDemo/dummy/del?dummyName=Alice001 BODY { "userId": 1, "userName": "Alice" }
   PUT http://localhost:8080/SpringRestDemo/dummy/upd?userId=102&userName=Alice02 BODY { "userId": 103, "userName": "Alice03" }  
   GET http://localhost:8080/SpringRestDemo/dummy/list
  POST http://localhost:8080/SpringRestDemo/dummy/page/3/2 BODY { "userId": "1", "userName": "Alice" } 
 
  POST http://localhost:8080/SpringRestDemo/dummy/uploadDoc BODY form-data
   GET http://localhost:8080/SpringRestDemo/dummy/downloadLog
   GET http://localhost:8080/SpringRestDemo/dummy/sysInfo 
 
 GET http://localhost:8080/SpringRestDemo/dummy/getUser1?userId=100&userName=Alice张三
 GET http://localhost:8080/SpringRestDemo/dummy/getUser2/Alice张三
 GET http://localhost:8080/SpringRestDemo/dummy/getUser3?userName=Alice张三
*/
@Controller
@RequestMapping(value = "/dummy")
public class DummyController extends BaseController {
    private Log log = LogFactory.getLog(DummyController.class);
    
    static final String RESPONSE_TYPE    = "application/json;charset=UTF-8"; //Response MIME type
    
    @Autowired
    private DummyService dummyService;

    @RequestMapping(value = "/list", method = RequestMethod.GET) //produces = "application/json; charset=utf-8", text/plain
    public @ResponseBody ResponseVO list() {
        return process(new AppCallable() {
            public Object run() throws ServiceException {
                return dummyService.list();
            }
        });
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public @ResponseBody ResponseVO add(final @RequestBody DummyVO dummyVO) {
        return process(new AppRunnable() {
            public void run() throws ServiceException {
                log.info("addUser(): " + dummyVO);
                dummyService.addDummy(dummyVO);
            }
        });
    }
    
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    public @ResponseBody ResponseVO del(final @RequestBody DummyVO dummyVO) {
        return process(new AppRunnable() {
            public void run() throws ServiceException {
                log.info("del(): " + dummyVO);
            }
        });
    }
    
    @RequestMapping(value = "/upd", method = RequestMethod.PUT)
    public @ResponseBody ResponseVO upd(final @RequestBody DummyVO dummyVO) {
        return process(new AppRunnable() {
            public void run() throws ServiceException {
                log.info("upd(): " + dummyVO);
            }
        });
    }
    
    @RequestMapping(path = "/page/{pageSize}/{pageIndex}", method = RequestMethod.POST, produces = RESPONSE_TYPE)
    public @ResponseBody ResponseVO findDummyPage(@RequestBody DummyVO param, PageVO pageVO) {
        return process(new AppCallable() {
            public Object run() throws ServiceException {
                return dummyService.findDummyPage(param, pageVO);
            }
        });
    }  
    
    @RequestMapping(path = "/downloadLog", method = RequestMethod.GET, produces = RESPONSE_TYPE)
    public @ResponseBody ResponseVO downloadLog() {
        return process(new AppRunnable() {
            public void run() throws ServiceException {
                dummyService.downloadLog();
            }
        });
    }

    @RequestMapping(path = "/uploadDoc", consumes = "multipart/form-data", method = RequestMethod.POST, produces = RESPONSE_TYPE)
    public @ResponseBody ResponseVO uploadDoc(@RequestParam MultipartFile multiFile, @RequestParam String userName) {
        return process(new AppRunnable() {
            public void run() throws ServiceException {
                log.info("UserName: " + userName);
                try (InputStream in = multiFile.getInputStream()) {
                     FileUtils.copyInputStreamToFile(in, new File("c:/" + multiFile.getOriginalFilename()));
                } catch (IOException e) {
                    log.error(e.getMessage());
                    throw new ServiceException("ERR_UPLOAD");
                }                
            }
        });
    }
    
    @RequestMapping(path = "/sysInfo", method = RequestMethod.GET, produces = RESPONSE_TYPE)
    public @ResponseBody ResponseVO sysInfo() {
        
        return process(new AppCallable() {
            public Object run() throws ServiceException {
                return dummyService.getSysInfo();
            }
        });
    }
    
    //Fill in @RequestParam to DummyVO
    @RequestMapping(value = "/getUser1", method = RequestMethod.GET)
    public @ResponseBody ResponseVO getUser(DummyVO dummyVO) {
        log.info("getUser1(): " + dummyVO);

        return process(new AppCallable() {
            public Object run() throws ServiceException {
                return dummyService.getUser(dummyVO.getUserName());
            }
        });
    }

    @RequestMapping(value = "/getUser2/{userName}", method = RequestMethod.GET)
    public @ResponseBody ResponseVO getUser2(final @PathVariable String userName) {
        log.info("getUser2(): " + userName);

        return process(new AppCallable() {
            public Object run() throws ServiceException {
                return dummyService.getUser(userName);
            }
        });
    }
    
    @RequestMapping(value = "/getUser3", method = RequestMethod.GET)
    public @ResponseBody ResponseVO getUser3(final @RequestParam String userName) {
        log.info("getUser3(): " + userName);

        return process(new AppCallable() {
            public Object run() throws ServiceException {
                return dummyService.getUser(userName);
            }
        });
    }    
}