package com.zero.srd.service;

import java.util.List;
import java.util.Map;

import com.zero.srd.core.PageResultVO;
import com.zero.srd.core.PageVO;
import com.zero.srd.core.ServiceException;
import com.zero.srd.vo.DummyVO;

public interface DummyService {
    PageResultVO findDummyPage(DummyVO param, PageVO pageVO);
    
    Map<String, String> getSysInfo();
    
    DummyVO getUser(String userName);

    List<DummyVO> list();

    void addDummy(DummyVO vo) throws ServiceException;
    
    void addUsers() throws ServiceException;
    
    void downloadLog() throws ServiceException;    
}