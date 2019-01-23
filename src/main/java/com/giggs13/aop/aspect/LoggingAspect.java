package com.giggs13.aop.aspect;

import com.giggs13.aop.entity.Account;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Aspect
@Component
@Order(2)
public class LoggingAspect {

    private static final Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Before("com.giggs13.aop.aspect.AopExpressions.daoPackageExcludingGettersAndSetters()")
    private void beforeAddAccountAdvice(JoinPoint joinPoint) {
        logger.info("\n---> Executing @Before advice on a method");

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        logger.info(methodSignature.toString());

        Arrays.stream(joinPoint.getArgs())
                .forEach(System.out::println);
    }

    @AfterReturning(
            pointcut = "com.giggs13.aop.aspect.AopExpressions.findAccounts()",
            returning = "result")
    private void afterReturningFindAccountsAdvice(JoinPoint joinPoint,
                                                  List<Account> result) {
        String method = joinPoint.getSignature().toShortString();
        logger.info("\n<--- Executing @AfterReturning advice on a method " + method);

        logger.info("Result is: " + result + "\n");
        convertAccountNamesToUpperCase(result);
    }

    private void convertAccountNamesToUpperCase(List<Account> result) {
        result.forEach(account -> account.setName(
                Optional.ofNullable(account.getName())
                        .orElse("Unknown Person")
                        .toUpperCase()));
    }

    @AfterThrowing(
            pointcut = "com.giggs13.aop.aspect.AopExpressions.findAccounts()",
            throwing = "error")
    private void afterThrowingFindAccountsAdvice(JoinPoint joinPoint,
                                                 Throwable error) {
        String method = joinPoint.getSignature().toShortString();
        logger.info("\n<--- Executing @AfterThrowing advice on a method " + method);

        logger.info("Error is: " + error + "\n");
    }

    @After("com.giggs13.aop.aspect.AopExpressions.findAccounts()")
    private void afterFinallyFindAccountsAdvice(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().toShortString();
        logger.info("\n<--- Executing @After advice on a method " + method);
    }

    @Around("execution(* com.giggs13.aop.service.*.getFortune(..))")
    private Object aroundGetFortune(ProceedingJoinPoint proceedingJoinPoint)
            throws Throwable {
        String method = proceedingJoinPoint.getSignature().toShortString();
        logger.info("\n<--- Executing @Around advice on a method " + method);

        StopWatch watcher = new StopWatch();
        watcher.start();
        Object result = proceedingJoinPoint.proceed();
        watcher.stop();
        logger.info("\n<--- Duration: " + watcher.getTotalTimeMillis());

        return result;
    }
}
