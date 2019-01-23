package com.giggs13.aop;

import com.giggs13.aop.service.TrafficFortuneService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.logging.Logger;

public class AroundWithLoggerDemoApp {

    private static final Logger logger = Logger.getLogger(AroundWithLoggerDemoApp.class.getName());

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext(DemoConfig.class);

        TrafficFortuneService trafficFortuneService = context.getBean(TrafficFortuneService.class);

        String data = trafficFortuneService.getFortune();
        logger.info("\nMy fortune is: " + data);

        logger.info("\nMain Program is finished");

        context.close();
    }
}
