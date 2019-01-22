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

@Aspect
@Component
@Order(2)
public class LoggingAspect {

    @Before("com.giggs13.aop.aspect.AopExpressions.daoPackageExcludingGettersAndSetters()")
    private void beforeAddAccountAdvice(JoinPoint joinPoint) {
        System.out.println("\n---> Executing @Before advice on a method");

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        System.out.println(methodSignature);

        Arrays.stream(joinPoint.getArgs())
                .forEach(System.out::println);
    }

    @AfterReturning(
            pointcut = "com.giggs13.aop.aspect.AopExpressions.findAccounts()",
            returning = "result")
    private void afterReturningFindAccountsAdvice(JoinPoint joinPoint,
                                                  List<Account> result) {
        String method = joinPoint.getSignature().toShortString();
        System.out.println("\n<--- Executing @AfterReturning advice on a method " + method);

        System.out.println("Result is: " + result + "\n");
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
        System.out.println("\n<--- Executing @AfterThrowing advice on a method " + method);

        System.out.println("Error is: " + error + "\n");
    }

    @After("com.giggs13.aop.aspect.AopExpressions.findAccounts()")
    private void afterFinallyFindAccountsAdvice(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().toShortString();
        System.out.println("\n<--- Executing @After advice on a method " + method);
    }

    @Around("execution(* com.giggs13.aop.service.*.getFortune(..))")
    private Object aroundGetFortune(ProceedingJoinPoint proceedingJoinPoint)
            throws Throwable {
        String method = proceedingJoinPoint.getSignature().toShortString();
        System.out.println("\n<--- Executing @Around advice on a method " + method);

        StopWatch watcher = new StopWatch();
        watcher.start();
        Object result = proceedingJoinPoint.proceed();
        watcher.stop();
        System.out.println("\n<--- Duration: " + watcher.getTotalTimeMillis());

        return result;
    }
}
