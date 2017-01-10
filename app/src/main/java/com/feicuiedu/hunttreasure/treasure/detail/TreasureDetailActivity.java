package com.feicuiedu.hunttreasure.treasure.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.feicuiedu.hunttreasure.R;
import com.feicuiedu.hunttreasure.components.TreasureView;
import com.feicuiedu.hunttreasure.treasure.Treasure;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TreasureDetailActivity extends AppCompatActivity {

    private static final String KEY_TREASURE = "key_treasure";
    @BindView(R.id.iv_navigation)
    ImageView mIvNavigation;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;
    @BindView(R.id.treasureView)
    TreasureView mTreasureView;
    @BindView(R.id.tv_detail_description)
    TextView mTvDetailDescription;
    private Treasure mTreasure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_detail);
    }

    public static void open(Context context, Treasure treasure) {
        Intent intent = new Intent(context, TreasureDetailActivity.class);
        intent.putExtra(KEY_TREASURE, treasure);
        context.startActivity(intent);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);

        mTreasure = (Treasure) getIntent().getSerializableExtra(KEY_TREASURE);

        // ActionBar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mTreasure.getTitle());

        // TreasureView
        mTreasureView.bindTreasure(mTreasure);

        initMapView();



    }

    // 宝藏数据的地图展示
    private void initMapView() {

        LatLng target = new LatLng(mTreasure.getLatitude(), mTreasure.getLongitude());

        MapStatus mapStatus = new MapStatus.Builder()
                .target(target)
                .rotate(0)
                .zoom(18)
                .overlook(-20)
                .build();

        BaiduMapOptions options = new BaiduMapOptions()
                .mapStatus(mapStatus)
                .compassEnabled(false)
                .zoomControlsEnabled(false)
                .rotateGesturesEnabled(false)
                .scrollGesturesEnabled(false)
                .zoomGesturesEnabled(false);
        MapView mapView = new MapView(this, options);

        mFrameLayout.addView(mapView);

        BaiduMap map = mapView.getMap();

        BitmapDescriptor dot = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded);

        MarkerOptions option = new MarkerOptions()
                .position(target)
                .icon(dot)
                .anchor(0.5f,0.5f);
        map.addOverlay(option);

    }

    @OnClick(R.id.iv_navigation)
    public void onClick() {
    }
}
