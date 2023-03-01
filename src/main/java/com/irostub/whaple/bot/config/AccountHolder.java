package com.irostub.whaple.bot.config;

import org.springframework.util.Assert;

public class AccountHolder {
    private static final ThreadLocal<AccountHoldInstance> holdInstance = new ThreadLocal<>();

    public void clearUserIdHolder() {
        holdInstance.remove();
    }
    public AccountHoldInstance getUser(){
        AccountHoldInstance user = holdInstance.get();
        if (user == null) {
            user = createEmptyUserId();
            holdInstance.set(user);
        }
        return user;
    }

    public void setUser(AccountHoldInstance user) {
        Assert.notNull(user, "Only non-null String instances are permitted");
        holdInstance.set(user);
    }

    public AccountHoldInstance createEmptyUserId(){
        return new AccountHoldInstance();
    }

}
