package com.feicuiedu.hunttreasure.treasure.map;

import com.baidu.mapapi.model.LatLng;
import com.feicuiedu.hunttreasure.treasure.Treasure;

import java.util.List;

/**
 * Created by gqq on 17/1/4.
 */

public interface MapMVPView {

    void showMessage(String msg);

    void setData(List<Treasure> list);

}
