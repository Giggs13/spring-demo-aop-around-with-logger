package com.giggs13.aop.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class AopExpressions {

    @Pointcut("execution(* com.giggs13.aop.dao.*.*(..))")
    void daoPackage() {
    }

    @Pointcut("execution(* com.giggs13.aop.dao.*.get*(..))")
    void getter() {
    }

    @Pointcut("execution(* com.giggs13.aop.dao.*.set*(..))")
    void setter() {
    }

    @Pointcut("daoPackage() && !(getter() || setter())")
    void daoPackageExcludingGettersAndSetters() {
    }

    @Pointcut("execution(* com.giggs13.aop.dao.AccountDAO.findAccounts(..))")
    void findAccounts() {
    }
}
