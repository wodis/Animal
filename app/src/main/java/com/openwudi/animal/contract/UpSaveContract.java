package com.openwudi.animal.contract;

import com.openwudi.animal.base.BaseModel;
import com.openwudi.animal.base.BasePresenter;
import com.openwudi.animal.base.BaseView;
import com.openwudi.animal.model.UpObject;

import java.util.List;

/**
 * Created by diwu on 17/7/19.
 */

public interface UpSaveContract {
    interface Model extends BaseModel {

    }

    interface View extends BaseView {

        void setData(List<UpObject> list);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
    }
}
