package com.feicuiedu.hunttreasure.treasure.map;

import android.os.AsyncTask;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gqq on 17/1/4.
 */

public class MapPresenter {

    private MapMVPView mMapMVPView;

    public MapPresenter(MapMVPView mapMVPView) {
        mMapMVPView = mapMVPView;
    }

    public void getTreasure(final LatLng latLng){

        new AsyncTask<Void, Void, List<LatLng>>() {
            @Override
            protected List<LatLng> doInBackground(Void... params) {

                List<LatLng> list = new ArrayList<LatLng>();
                for (int i = 0; i < 5; i++) {
                    list.add(new LatLng(latLng.latitude+(((double)i+1)/1000),latLng.longitude-(double)((i+1)/1000)));
            }
                return list;
            }

            @Override
            protected void onPostExecute(List<LatLng> latLngs) {
                super.onPostExecute(latLngs);
                mMapMVPView.showMessage("宝藏数据加载完毕");
                mMapMVPView.setData(latLngs);
            }
        }.execute();
    }
}
