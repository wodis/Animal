package com.openwudi.animal.view.pickerview.config;

import android.os.Parcel;
import android.os.Parcelable;

import com.openwudi.animal.view.pickerview.data.Type;

/**
 * Created by jzxiang on 16/5/15.
 */
public class DefaultConfig implements Parcelable {
    public static final Type TYPE = Type.ALL;
    public static final int COLOR = 0XFFE60012;
    public static final int TOOLBAR_TV_COLOR = 0xFFFFFFFF;
    public static final int TV_NORMAL_COLOR = 0xFF999999;
    public static final int TV_SELECTOR_COLOR = 0XFF404040;
    public static final int TV_SIZE = 12;
    public static final boolean CYCLIC = true;
    public static String CANCEL = "取消";
    public static String SURE = "确定";
    public static String TITLE = "TimePicker";
    public static String YEAR = "年";
    public static String MONTH = "月";
    public static String DAY = "日";
    public static String HOUR = "时";
    public static String MINUTE = "分";


    protected DefaultConfig(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DefaultConfig> CREATOR = new Creator<DefaultConfig>() {
        @Override
        public DefaultConfig createFromParcel(Parcel in) {
            return new DefaultConfig(in);
        }

        @Override
        public DefaultConfig[] newArray(int size) {
            return new DefaultConfig[size];
        }
    };
}
