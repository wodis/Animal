package com.openwudi.animal.contract;

import com.openwudi.animal.base.BaseModel;
import com.openwudi.animal.base.BasePresenter;
import com.openwudi.animal.base.BaseView;
import com.openwudi.animal.model.DataAcquisition;

import java.util.List;

/**
 * Created by diwu on 17/7/20.
 */

public interface HistoryContract {
    interface Model extends BaseModel {

    }

    interface View extends BaseView {
        void setData(List<DataAcquisition> data);

        void addData(List<DataAcquisition> data);

        void setTitle(String title);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
    }
}
