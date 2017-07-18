package com.openwudi.animal.contract.model;

import com.alibaba.fastjson.JSON;
import com.openwudi.animal.base.AnimalApplication;
import com.openwudi.animal.contract.UpContract;
import com.openwudi.animal.db.DaoSession;
import com.openwudi.animal.db.UpEntity;
import com.openwudi.animal.db.UpEntityDao;
import com.openwudi.animal.model.Animal;
import com.openwudi.animal.model.DataAcquisition;
import com.openwudi.animal.model.Item;

import java.util.Date;
import java.util.Set;

/**
 * Created by diwu on 17/7/14.
 */

public class UpModel implements UpContract.Model {

    @Override
    public void saveDataAcquisition(Animal animal,
                                    DataAcquisition dataAcquisition,
                                    Item qixidi,
                                    Set<Item> zhuangtai,
                                    Item juli,
                                    Item fangwei,
                                    Item weizhi) {

        UpEntity entity = new UpEntity();
        entity.setAnimal(JSON.toJSONString(animal));
        entity.setData(JSON.toJSONString(dataAcquisition));
        entity.setDate(new Date());
        entity.setQixidi(JSON.toJSONString(qixidi));
        entity.setZhuangtai(JSON.toJSONString(zhuangtai));
        entity.setJuli(JSON.toJSONString(juli));
        entity.setFangwei(JSON.toJSONString(fangwei));
        entity.setWeizhi(JSON.toJSONString(weizhi));

        DaoSession daoSession = AnimalApplication.INSTANCE.getDaoSession();
        UpEntityDao dao = daoSession.getUpEntityDao();
        dao.insert(entity);
    }
}
