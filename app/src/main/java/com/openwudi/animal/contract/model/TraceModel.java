package com.openwudi.animal.contract.model;

import com.openwudi.animal.base.AnimalApplication;
import com.openwudi.animal.contract.TraceContract;
import com.openwudi.animal.db.DaoSession;
import com.openwudi.animal.db.GPSData;
import com.openwudi.animal.db.GPSDataDao;
import com.openwudi.animal.manager.AccountManager;

import java.util.Date;

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
        dao.insert(data);
    }
}
