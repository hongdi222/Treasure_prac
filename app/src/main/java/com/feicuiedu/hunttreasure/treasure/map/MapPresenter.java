package com.feicuiedu.hunttreasure.treasure.map;

import android.os.AsyncTask;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.feicuiedu.hunttreasure.net.NetClient;
import com.feicuiedu.hunttreasure.treasure.Area;
import com.feicuiedu.hunttreasure.treasure.Treasure;
import com.feicuiedu.hunttreasure.treasure.TreasureRepo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gqq on 17/1/4.
 */

public class MapPresenter {

    private MapMVPView mMapMVPView;
    private Area mArea;

    public MapPresenter(MapMVPView mapMVPView) {
        mMapMVPView = mapMVPView;
    }

    public void getTreasure(Area area){

        if (TreasureRepo.getInstance().isCached(area)){
            return;
        }

        this.mArea = area;

        Call<List<Treasure>> listCall = NetClient.getInstances().getTreasureApi().getTreasureInArea(area);
        listCall.enqueue(mCallback);
    }

    private Callback<List<Treasure>> mCallback = new Callback<List<Treasure>>() {

        @Override
        public void onResponse(Call<List<Treasure>> call, Response<List<Treasure>> response) {
            if (response.isSuccessful()){
                List<Treasure> list = response.body();
                if (list==null){
                    mMapMVPView.showMessage("宝藏数据加载发生未知错误");
                    return;
                }
                TreasureRepo.getInstance().addTreasure(list);
                TreasureRepo.getInstance().cache(mArea);
                mMapMVPView.setData(list);
            }
        }

        @Override
        public void onFailure(Call<List<Treasure>> call, Throwable t) {
            mMapMVPView.showMessage("请求失败"+t.getMessage());
        }
    };
}
