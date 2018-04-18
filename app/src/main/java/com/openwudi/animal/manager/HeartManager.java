package com.openwudi.animal.manager;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.blankj.utilcode.utils.ThreadPoolUtils;
import com.openwudi.animal.db.MessageEntity;
import com.openwudi.animal.db.manager.MessageEntityManager;
import com.openwudi.animal.event.NewMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by diwu on 16/7/19.
 */
public class HeartManager {

    private static ThreadPoolUtils poolUtils;

    private static final int HEART_BEAT_INTERVAL = 30000;
    private Handler handler;

    private static HeartManager instance = null;

    private HeartManager() {
        poolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.SingleThread, 1);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<com.openwudi.animal.model.Message> list = new ArrayList<>();
                try {
                    list = ApiManager.listMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                    resendHeart();
                    return;
                }

                List<String> fids = new ArrayList<>();
                for (com.openwudi.animal.model.Message message : list) {
                    fids.add(message.getId());
                }

                boolean same = MessageEntityManager.checkSame(fids);
                sendBroadcast(same);

                resendHeart();
            }
        };

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 1) {
                    poolUtils.execute(runnable);
                }
                return false;
            }
        });
    }

    public void resendHeart() {
        handler.removeMessages(1);
        handler.sendEmptyMessageDelayed(1, HEART_BEAT_INTERVAL);
    }

    public static void sendBroadcast(boolean same) {
        EventBus.getDefault().post(new NewMessageEvent(same));
    }

    public static HeartManager getInstance() {
        if (instance == null) {
            synchronized (HeartManager.class) {
                if (instance == null) {
                    instance = new HeartManager();
                }
            }
        }

        return instance;
    }
}
