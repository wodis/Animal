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
        int getTotal();

        int getHealthNum();

        int getIllNum();

        int getDeathNum();

        String illDesc();

        String deathDesc();

        String bubao();

        void setQixidi(String qixidi);

        void setZhuangTai(String string);

        void setJuli(String string);

        void setFangwei(String string);

        void setWeizhi(String string);

        void setTime(String string);

        void setGps(String string);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
    }
}
