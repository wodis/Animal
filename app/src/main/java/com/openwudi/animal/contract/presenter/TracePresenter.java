package com.openwudi.animal.contract.presenter;

import com.blankj.utilcode.utils.EmptyUtils;
import com.blankj.utilcode.utils.ThreadPoolUtils;
import com.openwudi.animal.contract.TraceContract;

import java.util.UUID;

/**
 * Created by diwu on 17/7/24.
 */

public class TracePresenter extends TraceContract.Presenter {

    static String uuid;
    ThreadPoolUtils poolUtils;

    @Override
    public void onStart() {
        poolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.SingleThread, 1);
        if (EmptyUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
    }

    public void saveGps(final double lat, final double lng) {
        poolUtils.execute(new Runnable() {
            @Override
            public void run() {
                mModel.save2Db(lat, lng, uuid);
            }
        });
    }
}
