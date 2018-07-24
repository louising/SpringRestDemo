package com.zero.srd.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class BaseServiceImpl {
    private static Log log = LogFactory.getLog(BaseServiceImpl.class);

    protected PageResultVO doPagedQuery(Object dao, String daoMethodName, Object parameterVO, PageVO pageVO) throws ServiceException {
        PageResultVO pagedResult = null;
        try {
            //1) Get recordCount 
            Method countMethod = null;
            Integer recordCount = 0;
            if (parameterVO != null) {
                countMethod = dao.getClass().getDeclaredMethod(daoMethodName + "Count", parameterVO.getClass());
                recordCount = (Integer) countMethod.invoke(dao, parameterVO);
            } else {
                countMethod = dao.getClass().getDeclaredMethod(daoMethodName + "Count");
                recordCount = (Integer) countMethod.invoke(dao);
            }

            //2) Fill PageVO
            int startIndex = pageVO.getPageSize() * (pageVO.getPageIndex() - 1); //for MySQL 
            pageVO.setStartIndex(startIndex);

            /* for Oracle
            int startIndex = pageVO.getPageSize() * (pageVO.getPageIndex() - 1) + 1; //for Oracle
            int endIndex = startIndex + (pageVO.getPageSize() - 1);
            if (endIndex > recordCount)
            endIndex = recordCount;
            
            pageVO.setStartIndex(startIndex);
            pageVO.setEndIndex(endIndex);
            */

            pageVO.setRecordCount(recordCount);

            //3) Fill query parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("startIndex", pageVO.getStartIndex());

            parameters.put("pageSize", pageVO.getPageSize()); //for MySQL
            //parameters.put("endIndex", pageVO.getEndIndex()); //for Oracle

            if (parameterVO != null) {
                Field[] fields = parameterVO.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    parameters.put(field.getName(), field.get(parameterVO));
                }
            }

            //4) Do query
            Method queryMethod = dao.getClass().getDeclaredMethod(daoMethodName, Map.class);
            Object records = queryMethod.invoke(dao, parameters);
            pagedResult = new PageResultVO(pageVO, records);
        } catch (Exception e) {
            log.error("{}", e);
            throw new ServiceException("Query Page Error");
        }
        return pagedResult;
    }
}
