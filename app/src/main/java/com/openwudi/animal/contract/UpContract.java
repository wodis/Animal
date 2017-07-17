package com.openwudi.animal.contract;

import com.openwudi.animal.base.BaseModel;
import com.openwudi.animal.base.BasePresenter;
import com.openwudi.animal.base.BaseView;

/**
 * Created by diwu on 17/7/14.
 */

public interface UpContract {
    interface Model extends BaseModel {
    }

    interface View extends BaseView {
        void setQixidi(String qixidi);

        void setZhuangTai(String string);

        void setJuli(String string);

        void setFangwei(String string);

        void setWeizhi(String string);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
    }
}