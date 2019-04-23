package com.openwudi.animal.db.manager;

import com.openwudi.animal.base.AnimalApplication;
import com.openwudi.animal.db.TelEntity;
import com.openwudi.animal.db.TelEntityDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diwu on 2017/8/29.
 */

public class TelEntityManager {
    public static List<TelEntity> listAll(){
        TelEntityDao dao = AnimalApplication.INSTANCE.getDaoSession().getTelEntityDao();
        List<TelEntity> list = dao.queryBuilder().list();
        if (list == null){
            list = new ArrayList<>();
        }
        return list;
    }

    public static void add(TelEntity entity){
        TelEntityDao dao = AnimalApplication.INSTANCE.getDaoSession().getTelEntityDao();
        dao.insert(entity);
    }

    public static void update(TelEntity entity){
        TelEntityDao dao = AnimalApplication.INSTANCE.getDaoSession().getTelEntityDao();
        dao.update(entity);
    }

    public static void delete(Long id){
        TelEntityDao dao = AnimalApplication.INSTANCE.getDaoSession().getTelEntityDao();
        dao.deleteByKey(id);
    }
}
