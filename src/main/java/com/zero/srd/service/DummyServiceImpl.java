package com.zero.srd.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zero.srd.core.BaseServiceImpl;
import com.zero.srd.core.PageResultVO;
import com.zero.srd.core.PageVO;
import com.zero.srd.core.ServiceException;
import com.zero.srd.dao.DummyDao;
import com.zero.srd.util.BaseUtil;
import com.zero.srd.util.FileUtil;
import com.zero.srd.util.IoUtil;
import com.zero.srd.util.ZipUtils;
import com.zero.srd.vo.DummyVO;

@Component
public class DummyServiceImpl extends BaseServiceImpl implements DummyService {
    private Log log = LogFactory.getLog(DummyServiceImpl.class);
    
    final static String LOG_DIR = "c:/SpringRestDemoLogs"; //refer to logback.xml
    final static String LOG_ZIP_FULL_FILE_NAME = LOG_DIR + "logs.zip";    
    
    @Autowired
    private DummyDao userDao;

    public DummyVO getUser(String userName) {
        return new DummyVO(1000, userName+ "-China中国", true);
    }

    public List<DummyVO> list() {
        return userDao.list();
    }

    public void addDummy(DummyVO vo) throws ServiceException {
        userDao.addDummy(vo);
    }    
    
    //(rollbackFor = ServiceException.class), by default roll back when RuntimeException occurs
    //@Transactional
    public void addUsers() throws ServiceException {
        int i = 10;
        userDao.addDummy(new DummyVO("AAA"));
        if (i > 1)
            throw new ServiceException("ERR_ADD_USER");
    }
    
    public PageResultVO findDummyPage(DummyVO param, PageVO pageVO) throws ServiceException {
        return doPagedQuery(userDao, "findDummyPage", param, pageVO);
    }    
    
    public Map<String, String> getSysInfo() {
        Map<String, String> map = BaseUtil.getSysInfo();
        return map;
    }
    
    public void downloadLog() throws ServiceException {
        //1) Log
        File file = new File(LOG_DIR);
        File[] files = file.listFiles(); //if file not exist, file.listFiles() will return null, if file.exist(), will return [] 
        if (files == null || files.length == 0) {
            throw new ServiceException("No log files");
        }

        StringBuffer sb = new StringBuffer("logs\n");
        for (int i = 0; i < files.length; i++) {
            sb.append(i + ": " + files[i].getName() + "\n");
        }
        log.info(sb.toString());

        //2) Download
        File zipFile = new File(LOG_ZIP_FULL_FILE_NAME);
        try {
            ZipUtils.compressFiles(files, zipFile);
            IoUtil.writeFile2Response(zipFile);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        } finally {
            FileUtil.deleteFile(zipFile);
        }
    }    
}
