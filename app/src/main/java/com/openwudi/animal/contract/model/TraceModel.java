package com.openwudi.animal.contract.model;

import com.blankj.utilcode.utils.EmptyUtils;
import com.openwudi.animal.base.AnimalApplication;
import com.openwudi.animal.contract.TraceContract;
import com.openwudi.animal.db.DaoSession;
import com.openwudi.animal.db.GPSData;
import com.openwudi.animal.db.GPSDataDao;
import com.openwudi.animal.db.UpEntityDao;
import com.openwudi.animal.manager.AccountManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by diwu on 17/7/24.
 */

public class TraceModel implements TraceContract.Model {

    @Override
    public void save2Db(double lat, double lng, String uuid) {
        DaoSession daoSession = AnimalApplication.INSTANCE.getDaoSession();
        GPSDataDao dao = daoSession.getGPSDataDao();

        GPSData data = new GPSData();
        data.setCreateTime(new Date());
        data.setLatitude(lat);
        data.setLongtitude(lng);
        data.setTerminalId(AccountManager.getAccount().getTerminalId());
        data.setUserId(AccountManager.getAccount().getUserId());

        if (EmptyUtils.isEmpty(dao.queryBuilder().where(GPSDataDao.Properties.CreateTime.eq(data.getCreateTime())).list())) {
            dao.insert(data);
        }
    }


    @Override
    public List<GPSData> list() {
        DaoSession daoSession = AnimalApplication.INSTANCE.getDaoSession();
        GPSDataDao dao = daoSession.getGPSDataDao();

        List<GPSData> list = dao.queryBuilder().limit(1000).orderDesc(GPSDataDao.Properties.Id).list();
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    @Override
    public void deleteById(long id) {
        DaoSession daoSession = AnimalApplication.INSTANCE.getDaoSession();
        GPSDataDao dao = daoSession.getGPSDataDao();
        dao.deleteByKey(id);
    }
}
