package com.openwudi.animal.db.manager;

import com.blankj.utilcode.utils.EmptyUtils;
import com.openwudi.animal.base.AnimalApplication;
import com.openwudi.animal.db.MessageEntity;
import com.openwudi.animal.db.MessageEntityDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diwu on 2017/9/7.
 */

public class MessageEntityManager {
    public static void add(List<MessageEntity> entity) {
        MessageEntityDao dao = AnimalApplication.INSTANCE.getDaoSession().getMessageEntityDao();
        try {
            dao.insertOrReplaceInTx(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<MessageEntity> list() {
        MessageEntityDao dao = AnimalApplication.INSTANCE.getDaoSession().getMessageEntityDao();
        return dao.queryBuilder().list();
    }

    public static boolean checkSame(List<String> fids) {
        if (EmptyUtils.isEmpty(fids)) {
            return true;
        }
        List<MessageEntity> db = list();
        List<String> dbFid = new ArrayList<>();
        for (MessageEntity entity : db) {
            dbFid.add(entity.getFid());
        }

        for (String fid : fids) {
            if (!dbFid.contains(fid)) {
                return false;
            }
        }

        return true;
    }
}
