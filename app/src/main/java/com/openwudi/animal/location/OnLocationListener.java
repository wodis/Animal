package com.openwudi.animal.location;

import com.baidu.location.Address;

public interface OnLocationListener {
    void locSuccess(Address address, double latitude, double longitude, double altitude);
    void locError();
}
