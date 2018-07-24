package com.zero.srd.dao;

import java.util.List;
import java.util.Map;

import com.zero.srd.vo.DummyVO;

public interface DummyDao {
    List<DummyVO> list();
    
    void addDummy(DummyVO vo);
    
    List<DummyVO> findDummyPage(Map<String, Object> parameters);
    
    int findDummyPageCount(DummyVO paramaterVO);    
}
