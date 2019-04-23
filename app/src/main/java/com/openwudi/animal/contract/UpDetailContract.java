package com.openwudi.animal.contract;

import com.openwudi.animal.base.BaseModel;
import com.openwudi.animal.base.BasePresenter;
import com.openwudi.animal.base.BaseView;
import com.openwudi.animal.model.UpObject;

/**
 * Created by diwu on 17/7/20.
 */

public interface UpDetailContract {
    interface Model extends BaseModel {

    }

    interface View extends BaseView {
        void initView();

        void setObject(UpObject object);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
    }
}
