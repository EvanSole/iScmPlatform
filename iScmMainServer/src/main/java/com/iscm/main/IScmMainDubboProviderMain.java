package com.iscm.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;


public class IScmMainDubboProviderMain {

    static Logger _LOG = Logger.getLogger(IScmMainDubboProviderMain.class.getName());

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:main/spring-core.xml");
        context.start();
        System.out.println("IScmMainServer start------" + System.getProperty("spring.profiles.active"));
        new CountDownLatch(1).await();
    }
}
