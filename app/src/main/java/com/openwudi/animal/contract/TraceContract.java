package com.openwudi.animal.contract;

import com.openwudi.animal.base.BaseModel;
import com.openwudi.animal.base.BasePresenter;
import com.openwudi.animal.base.BaseView;
import com.openwudi.animal.db.GPSData;

import java.util.List;

/**
 * Created by diwu on 17/7/24.
 */

public interface TraceContract {
    interface Model extends BaseModel {

        void save2Db(double lat, double lng, String uuid);

        List<GPSData> list();

        void deleteById(long id);
    }

    interface View extends BaseView {
    }

    abstract class Presenter extends BasePresenter<Model, View> {
    }
}
