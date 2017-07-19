package com.openwudi.animal.db.manager;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.utils.EmptyUtils;
import com.openwudi.animal.base.AnimalApplication;
import com.openwudi.animal.db.UpEntity;
import com.openwudi.animal.db.UpEntityDao;
import com.openwudi.animal.model.Animal;
import com.openwudi.animal.model.DataAcquisition;
import com.openwudi.animal.model.Item;
import com.openwudi.animal.model.UpObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by diwu on 17/7/18.
 */

public class UpEntityManager {
    public static List<UpObject> listAll() {
        UpEntityDao dao = AnimalApplication.INSTANCE.getDaoSession().getUpEntityDao();
        List<UpEntity> entityList = dao.queryBuilder().orderDesc(UpEntityDao.Properties.Id).list();
        List<UpObject> list = new ArrayList<>();
        for (UpEntity entity : entityList) {
            Animal animal = null;
            DataAcquisition dataAcquisition = null;
            Item qixidi = null;
            Set<Item> zhuangtai = null;
            Item juli = null;
            Item fangwei = null;
            Item weizhi = null;
            if (EmptyUtils.isNotEmpty(entity.getAnimal())) {
                animal = JSON.parseObject(entity.getAnimal(), Animal.class);
            }
            if (EmptyUtils.isNotEmpty(entity.getData())) {
                dataAcquisition = JSON.parseObject(entity.getData(), DataAcquisition.class);
            }
            if (EmptyUtils.isNotEmpty(entity.getQixidi())) {
                qixidi = JSON.parseObject(entity.getQixidi(), Item.class);
            }
            if (EmptyUtils.isNotEmpty(entity.getZhuangtai())) {
                zhuangtai = JSON.parseObject(entity.getZhuangtai(), new TypeReference<Set<Item>>() {
                });
            }
            if (EmptyUtils.isNotEmpty(entity.getJuli())) {
                juli = JSON.parseObject(entity.getJuli(), Item.class);
            }
            if (EmptyUtils.isNotEmpty(entity.getFangwei())) {
                fangwei = JSON.parseObject(entity.getFangwei(), Item.class);
            }
            if (EmptyUtils.isNotEmpty(entity.getWeizhi())) {
                weizhi = JSON.parseObject(entity.getWeizhi(), Item.class);
            }
            UpObject o = new UpObject(entity.getId(), animal, dataAcquisition, qixidi, zhuangtai, juli, fangwei, weizhi);
            list.add(o);
        }
        return list;
    }

    public static void deleteById(List<Long> id) {
        if (EmptyUtils.isEmpty(id)){
            return;
        }
        UpEntityDao dao = AnimalApplication.INSTANCE.getDaoSession().getUpEntityDao();
        dao.deleteByKeyInTx(id);
    }
}
