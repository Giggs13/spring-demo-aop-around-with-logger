package com.giggs13.aop.dao;

import com.giggs13.aop.entity.Account;

import java.util.List;

public interface AccountDAO {

    void addAccount(Account account,
                    boolean vip);

    boolean doWork();

    List<Account> findAccounts(boolean tripWire);
}
