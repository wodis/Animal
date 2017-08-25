package com.openwudi.animal.db.manager;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.utils.EmptyUtils;
import com.openwudi.animal.base.AnimalApplication;
import com.openwudi.animal.db.AnimalEntity;
import com.openwudi.animal.db.AnimalEntityDao;
import com.openwudi.animal.model.Animal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by diwu on 2017/8/25.
 */

public class AnimalEntityManager {
    public static List<Animal> listAll() {
        List<Animal> result = new ArrayList<>();
        AnimalEntityDao dao = AnimalApplication.INSTANCE.getDaoSession().getAnimalEntityDao();
        List<AnimalEntity> list = dao.queryBuilder().orderDesc(AnimalEntityDao.Properties.Date).limit(100).list();
        if (EmptyUtils.isNotEmpty(list)) {
            for (AnimalEntity entity : list) {
                try {
                    Animal animal = JSON.parseObject(entity.getAnimal(), Animal.class);
                    result.add(animal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static void add(Animal animal) {
        try {
            AnimalEntityDao dao = AnimalApplication.INSTANCE.getDaoSession().getAnimalEntityDao();
            AnimalEntity entity = new AnimalEntity();
            entity.setAid(animal.getId());
            entity.setDate(new Date());
            entity.setAnimal(JSON.toJSONString(animal));
            dao.insert(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update(Animal animal){
        AnimalEntityDao dao = AnimalApplication.INSTANCE.getDaoSession().getAnimalEntityDao();
        List<AnimalEntity> list = dao.queryBuilder().where(AnimalEntityDao.Properties.Aid.eq(animal.getId())).limit(1).list();
        if (EmptyUtils.isNotEmpty(list)) {
            AnimalEntity entity = list.get(0);
            entity.setDate(new Date());
            dao.update(entity);
        } else {
            add(animal);
        }
    }
}
