package com.zero.srd.service;

import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;

//@Component
public class ScheduledJob {

    //cron="0 0 23 1/7 * ?" Run at 23:00:00 every 7 days since the first day each month
    @Scheduled(cron = "1/3 * * * * ?")
    public void doIt() {
        System.out.println("Schedule Task");
    }
}