package com.openwudi.animal.view.pickerview.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by jzxiang on 16/4/19.
 */
public class WheelCalendar implements Parcelable {

    public int year, month, day, hour, minute;

    private boolean noRange;

    public WheelCalendar(long millseconds) {
        initData(millseconds);
    }

    protected WheelCalendar(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        noRange = in.readByte() != 0;
    }

    public static final Creator<WheelCalendar> CREATOR = new Creator<WheelCalendar>() {
        @Override
        public WheelCalendar createFromParcel(Parcel in) {
            return new WheelCalendar(in);
        }

        @Override
        public WheelCalendar[] newArray(int size) {
            return new WheelCalendar[size];
        }
    };

    private void initData(long millseconds) {
        if (millseconds == 0) {
            noRange = true;
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(millseconds);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    public boolean isNoRange() {
        return noRange;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeByte((byte) (noRange ? 1 : 0));
    }
}
