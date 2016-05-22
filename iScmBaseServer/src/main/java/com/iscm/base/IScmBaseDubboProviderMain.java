package com.iscm.base;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;


public class IScmBaseDubboProviderMain {

    static Logger _LOG = Logger.getLogger(IScmBaseDubboProviderMain.class.getName());

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:base/spring-core.xml");
        context.start();
        System.out.println("IScmBaseServer start------" + System.getProperty("spring.profiles.active"));
        new CountDownLatch(1).await();
    }
}
