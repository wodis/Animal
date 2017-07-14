package com.openwudi.animal.manager;

import com.openwudi.animal.model.Account;

/**
 * Created by diwu on 17/6/27.
 */

public class AccountManager {
    private static Account account;

    public static void setAccount(Account acc) {
        account = acc;
        ApiManager.saveAccount(account);
    }

    public static Account getAccount() {
        if (account == null) {
            account = ApiManager.getAccount();
        }

        if (account != null) {
            return account;
        } else {
            return null;
        }
    }

    /**
     * 清空Session缓存
     */
    public static void clearAccount() {
        account = null;
        ApiManager.clearAccount();
    }
}
