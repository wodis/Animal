package com.openwudi.animal.contract;

import android.view.View;

import com.openwudi.animal.base.BaseModel;
import com.openwudi.animal.base.BasePresenter;
import com.openwudi.animal.base.BaseView;
import com.openwudi.animal.model.Animal;
import com.openwudi.animal.model.UpObject;
import com.yyydjk.library.DropDownMenu;

import java.util.List;

/**
 * Created by diwu on 17/7/19.
 */

public interface AnimalSelectContract {
    interface Model extends BaseModel {

    }

    interface View extends BaseView {
        DropDownMenu getDropDownMenu();

        android.view.View getContentView();

        void search(String name);

        void setData(List<Animal> animalList);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
    }
}
