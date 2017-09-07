package com.openwudi.animal.db.manager;

import com.openwudi.animal.base.AnimalApplication;
import com.openwudi.animal.db.AnimalServerEntity;
import com.openwudi.animal.db.AnimalServerEntityDao;
import com.openwudi.animal.model.Animal;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diwu on 2017/9/7.
 */

public class AnimalServerEntityManager {
    public static void add(List<Animal> orig) {
        List<AnimalServerEntity> list = new ArrayList<>();
        for (Animal animal : orig) {
            AnimalServerEntity entity = new AnimalServerEntity(animal);
            list.add(entity);
        }
        AnimalServerEntityDao dao = AnimalApplication.INSTANCE.getDaoSession().getAnimalServerEntityDao();
        dao.insertOrReplaceInTx(list);
    }

    public static List<Animal> getAnimalSelectList(String level, String fid, String keyword) {
        fid = fid.trim().replaceAll("0*$", "");
        AnimalServerEntityDao dao = AnimalApplication.INSTANCE.getDaoSession().getAnimalServerEntityDao();
        QueryBuilder<AnimalServerEntity> qb = dao.queryBuilder();
        qb.where(
                AnimalServerEntityDao.Properties.Level.eq(level),
                AnimalServerEntityDao.Properties.Fid.like(fid + "%"),
                qb.or(AnimalServerEntityDao.Properties.Name.like("%" + keyword + "%"),
                        AnimalServerEntityDao.Properties.Pinyin.like("%" + keyword + "%"),
                        AnimalServerEntityDao.Properties.PinyinInitials.like("%" + keyword + "%"))
        );
        List<AnimalServerEntity> list = qb.list();
        List<Animal> orig = new ArrayList<>();
        for (AnimalServerEntity entity : list) {
            Animal animal = new Animal(entity);
            orig.add(animal);
        }
        return orig;
    }
}
