package com.openwudi.animal.manager;

import com.blankj.utilcode.utils.TimeUtils;
import com.openwudi.animal.model.Account;

/**
 * Created by diwu on 17/6/27.
 */

public class AccountManager {
    private static final long INTERVAL = 1000 * 60 * 60 * 24;
    private static Account account;

    public static void setAccount(Account acc) {
        acc.setExpTime(TimeUtils.getCurTimeMills() + INTERVAL);
        account = acc;
        ApiManager.saveAccount(account);
    }

    public static Account getAccount() {
        if (account == null) {
            account = ApiManager.getAccount();
        }

        if (account != null) {
            if ((account.getExpTime() - TimeUtils.getCurTimeMills()) < 0) {
                return null;
            }
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
