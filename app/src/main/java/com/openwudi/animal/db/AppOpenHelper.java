package com.openwudi.animal.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.blankj.utilcode.utils.SPUtils;
import com.openwudi.animal.base.AnimalApplication;
import com.openwudi.animal.utils.Constants;

import org.greenrobot.greendao.database.Database;

/**
 * Created by diwu on 16/12/30.
 */

public class AppOpenHelper extends DaoMaster.OpenHelper {
    public AppOpenHelper(Context context, String name) {
        super(context, name);
    }

    public AppOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        // TODO: 16/12/30 do nothing
        DaoMaster.dropAllTables(db, true);
        DaoMaster.createAllTables(db, false);
        SPUtils spUtils = new SPUtils(AnimalApplication.INSTANCE, Constants.SP_CONFIG);
        spUtils.clear();
    }
}
