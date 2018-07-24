package com.zero.srd.core;

public interface AppRunnable extends AppTask {
    void run() throws ServiceException;
}