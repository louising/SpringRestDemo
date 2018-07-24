package com.zero.srd.core;

public interface AppCallable extends AppTask {
    Object run() throws ServiceException;
}
