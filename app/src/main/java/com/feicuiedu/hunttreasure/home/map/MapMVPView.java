package com.feicuiedu.hunttreasure.home.map;

import com.baidu.mapapi.model.LatLng;

import java.util.List;

/**
 * Created by gqq on 17/1/4.
 */

public interface MapMVPView {

    void showMessage(String msg);

    void setData(List<LatLng> list);

}
