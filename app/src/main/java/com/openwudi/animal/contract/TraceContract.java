package com.openwudi.animal.contract;

import com.openwudi.animal.base.BaseModel;
import com.openwudi.animal.base.BasePresenter;
import com.openwudi.animal.base.BaseView;

/**
 * Created by diwu on 17/7/24.
 */

public interface TraceContract {
    interface Model extends BaseModel {

        void save2Db(double lat, double lng, String uuid);
    }

    interface View extends BaseView {
    }

    abstract class Presenter extends BasePresenter<Model, View> {
    }
}
