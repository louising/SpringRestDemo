package com.zero.srd.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseController {
    private static Log log = LogFactory.getLog(BaseController.class);

    protected ResponseVO process(AppTask task) {
        ResponseVO result = new ResponseVO();

        try {
            checkUserLogin();

            if (task instanceof AppCallable)
                result.setData(((AppCallable) task).run());
            else
                ((AppRunnable) task).run();
        } catch (ServiceException e) {
            log.error(e.getErrCode());
            result.setStatusCode(e.getErrCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatusCode(BaseConstants.STATUS_ERR);
        }

        return result;
    }

    private void checkUserLogin() throws ServiceException {
        if (BaseConstants.isDebug)
            return;
        
        /*
        Object userInfo = SessionManager.getUser();
        if (userInfo == null)
            throw new ServiceException(STATUS_NOT_LOGIN);
        */
    }
}