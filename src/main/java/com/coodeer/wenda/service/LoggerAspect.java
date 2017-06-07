package com.coodeer.wenda.service;

import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

/**
 * Created by common on 2017/5/14.
 */
@Aspect
@Configuration
public class LoggerAspect {

    private static final Logger logger = Logger.getLogger(LoggerAspect.class);

    @Pointcut("execution(* com.coodeer.wenda.service.MyService.doSomeThing(..))")
    public void pointcut(){}


    @Before("pointcut()")
    public void before(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for(Object o : joinPoint.getArgs())
            sb.append("args : " + o + " | ");
        logger.info("before : " + sb);
    }

    @After("pointcut()")
    public void after(){
        logger.info("after");
    }
}
