package com.feicuiedu.hunttreasure.treasure.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.feicuiedu.hunttreasure.R;
import com.feicuiedu.hunttreasure.commons.ActivityUtils;
import com.feicuiedu.hunttreasure.components.TreasureView;
import com.feicuiedu.hunttreasure.treasure.Area;
import com.feicuiedu.hunttreasure.treasure.Treasure;
import com.feicuiedu.hunttreasure.treasure.TreasureRepo;
import com.feicuiedu.hunttreasure.treasure.detail.TreasureDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gqq on 17/1/2.
 */

public class MapFragment extends Fragment implements MapMVPView {


    private static final int BAIDU_READ_PHONE_STATE = 100;
    private static final int ACCESS_COARSE_LOCATION = 100;

    @BindView(R.id.iv_located)
    ImageView mIvLocated;
    @BindView(R.id.btn_HideHere)
    Button mBtnHideHere;
    @BindView(R.id.centerLayout)
    RelativeLayout mCenterLayout;
    @BindView(R.id.tv_currentLocation)
    TextView mTvCurrentLocation;
    @BindView(R.id.et_treasureTitle)
    EditText mEtTreasureTitle;
    @BindView(R.id.layout_bottom)
    FrameLayout mLayoutBottom;
    @BindView(R.id.map_frame)
    FrameLayout mMapFrame;
    @BindView(R.id.tv_satellite)
    TextView mTvSatellite;
    @BindView(R.id.treasureView)
    TreasureView mTreasureView;
    @BindView(R.id.hide_treasure)
    RelativeLayout mHideTreasure;

    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private static LatLng mMyCurrentLocation;
    private LatLng mCurrentStatus;
    private GeoCoder mGeoCoder;
    private ActivityUtils mActivityUtils;
    private Marker mCurrentMarker;
    private MapPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container);

//        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_DENIED) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG", "需要权限");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION);
        } else {
            Toast.makeText(getContext(), "不需要获取运行权限", Toast.LENGTH_SHORT).show();
            Log.i("TAG", "不需要权限");
        }

        mActivityUtils = new ActivityUtils(this);

        mPresenter = new MapPresenter(this);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initMapView();

        initLocation();

        initGeocoder();

    }

    private void initGeocoder() {
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(mGeoCoderResultListener);
    }

    private OnGetGeoCoderResultListener mGeoCoderResultListener = new OnGetGeoCoderResultListener() {

        // 正向地理编码
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        // 反向地理编码
        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null) {
                return;
            }
            String currentAddr = reverseGeoCodeResult.getAddress();
            mTvCurrentLocation.setText(currentAddr);

        }
    };

    private void initLocation() {

        // 激活定位图层
        mBaiduMap.setMyLocationEnabled(true);

        // 初始化定位核心类
        mLocationClient = new LocationClient(getContext());

        // 进行一些定位的一般常规性设置
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开GPS
//        option.setScanSpan(60000);// 扫描周期,设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setCoorType("bd09ll");// 百度坐标类型
        option.setLocationNotify(true);//设置是否当gps有效时按照1S1次频率输出GPS结果
        option.SetIgnoreCacheException(false);//设置是否收集CRASH信息，默认收集
        option.setIsNeedAddress(true);// 设置是否需要地址信息，默认不需要
        option.setIsNeedLocationDescribe(true);//设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mLocationClient.setLocOption(option);

        // 设置定位的监听
        mLocationClient.registerLocationListener(mBDLocationListener);

        // 开始定位
        mLocationClient.start();

    }

    private BDLocationListener mBDLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                mLocationClient.requestLocation();
                return;
            }

            double longitude = bdLocation.getLongitude();
            double latitude = bdLocation.getLatitude();
            mMyCurrentLocation = new LatLng(latitude, longitude);
            String myCurrentAddr = bdLocation.getAddrStr();

            Log.i("TAG", "当前的经纬度：" + longitude + "," + latitude + ",地址：" + myCurrentAddr);

            MyLocationData locationData = new MyLocationData.Builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .accuracy(100f)
                    .build();
            mBaiduMap.setMyLocationData(locationData);

            moveToLocation();

        }
    };

    private void initMapView() {

        MapStatus mapStatus = new MapStatus.Builder()
                .zoom(19)// 缩放的级别：3-21
                .overlook(0)// 俯仰的角度:0-－（－45）
                .build();

        BaiduMapOptions options = new BaiduMapOptions()
                .mapStatus(mapStatus) // 地图相关状态
                .compassEnabled(true) // 指南针
                .zoomGesturesEnabled(true) // 设置是否允许缩放手势
                .rotateGesturesEnabled(true) // 设置是否允许旋转手势，默认允许
                .scrollGesturesEnabled(true) // 设置是否允许拖拽手势，默认允许
                .scaleControlEnabled(false) // 设置是否显示比例尺控件
                .overlookingGesturesEnabled(false) // 设置是否允许俯视手势，默认允许
                .zoomControlsEnabled(false) // 设置是否显示缩放控件
                ;

        MapView mapView = new MapView(getContext(), options);

        // 在布局上添加地图控件：0代表添加到第一位：FrameLayout帧布局，有覆盖的效果
        mMapFrame.addView(mapView, 0);

        // 拿到地图的控制器
        mBaiduMap = mapView.getMap();

        mBaiduMap.setOnMapStatusChangeListener(mMapStatusChangeListener);

        mBaiduMap.setOnMarkerClickListener(mMarkerClickListener);
    }

    private BaiduMap.OnMarkerClickListener mMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

            if (mCurrentMarker != null) mCurrentMarker.setVisible(true);
            mCurrentMarker = marker;
            // 设置Marker不可见
            mCurrentMarker.setVisible(false);
            InfoWindow infoWindow = new InfoWindow(dot_expand, marker.getPosition(), 0, infoWindowClickListener);
            // 显示一个信息窗口(icon,位置,Y,监听)
            mBaiduMap.showInfoWindow(infoWindow);

            Treasure treasure = TreasureRepo.getInstance().getTreasure(marker.getExtraInfo().getInt("id"));
            mTreasureView.bindTreasure(treasure);

            changeUiMode(UI_MODE_SELECT);

            return true;
        }
    };

    private InfoWindow.OnInfoWindowClickListener infoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick() {

            changeUiMode(UI_MODE_NORMAL);
        }
    };

    private BaiduMap.OnMapStatusChangeListener mMapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {

            LatLng target = mapStatus.target;

            if (target != MapFragment.this.mCurrentStatus) {

                updateMapArea(target);

                ReverseGeoCodeOption geoCodeOption = new ReverseGeoCodeOption();
                geoCodeOption.location(target);

                mGeoCoder.reverseGeoCode(geoCodeOption);

                MapFragment.this.mCurrentStatus = target;

            }

        }
    };


    // 卫星按钮
    @OnClick(R.id.tv_satellite)
    public void switchMapType() {
        int mapType = mBaiduMap.getMapType();
        mapType = mapType == BaiduMap.MAP_TYPE_NORMAL ? BaiduMap.MAP_TYPE_SATELLITE : BaiduMap.MAP_TYPE_NORMAL;
        String msg = mapType == BaiduMap.MAP_TYPE_NORMAL ? "卫星" : "普通";
        mBaiduMap.setMapType(mapType);
        mTvSatellite.setText(msg);

    }

    // 指南针
    @OnClick(R.id.tv_compass)
    public void switchCompass() {
        boolean compassEnabled = mBaiduMap.getUiSettings().isCompassEnabled();
        mBaiduMap.getUiSettings().setCompassEnabled(!compassEnabled);
    }

    // 缩放的两个按钮
    @OnClick({R.id.iv_scaleUp, R.id.iv_scaleDown})
    public void scaleMap(View view) {
        switch (view.getId()) {
            case R.id.iv_scaleUp:
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
            case R.id.iv_scaleDown:
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
        }
    }

    // 定位
    @OnClick(R.id.tv_located)
    public void moveToLocation() {
        MapStatus mapStatus = new MapStatus.Builder()
                .target(mMyCurrentLocation)
                .overlook(0)
                .zoom(19)
                .rotate(0)
                .build();
        MapStatusUpdate mapStatuaUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.animateMapStatus(mapStatuaUpdate);
    }

    @OnClick(R.id.treasureView)
    public void clickTreasureView(){
        int id = mCurrentMarker.getExtraInfo().getInt("id");
        Treasure treasure = TreasureRepo.getInstance().getTreasure(id);
        TreasureDetailActivity.open(getContext(),treasure);
    }

    @OnClick(R.id.hide_treasure)
    public void clickHideTreasure(){

    }

    private void updateMapArea(LatLng latLng) {

        MapStatus mapStatus = mBaiduMap.getMapStatus();
        double lng = mapStatus.target.longitude;
        double lat = mapStatus.target.latitude;
        // 计算出你的Area  23.999  15.130
        //              24,23  ,  16,15去确定Area
        Area area = new Area();
        area.setMaxLat(Math.ceil(lat));  // lat向上取整
        area.setMaxLng(Math.ceil(lng));  // lng向上取速
        area.setMinLat(Math.floor(lat));  // lat向下取整
        area.setMinLng(Math.floor(lng));  // lng向下取整
        // 执行业务,根据Area去获取宝藏
        mPresenter.getTreasure(area);

    }

    private BitmapDescriptor dot = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_dot);
    private BitmapDescriptor dot_expand = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded);

    public void addMarker(LatLng latLng, int treasureId) {

        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.icon(dot);
        options.anchor(0.5f, 0.5f);

        Bundle bundle = new Bundle();
        bundle.putInt("id", treasureId);
        options.extraInfo(bundle);

        mBaiduMap.addOverlay(options);
    }

    public static LatLng getMyLocation() {
        return mMyCurrentLocation;
    }

    private static final int UI_MODE_NORMAL = 0;
    private static final int UI_MODE_SELECT = 1;
    private static final int UI_MODE_HIDE = 2;

    private static int mUIMode = UI_MODE_NORMAL;

    public void changeUiMode(int uiMode) {
        if (mUIMode == uiMode) return;
        mUIMode = uiMode;
        switch (uiMode) {
            case UI_MODE_NORMAL:
                if (mCurrentMarker != null) {
                    mCurrentMarker.setVisible(true);
                }
                mBaiduMap.hideInfoWindow();
                mLayoutBottom.setVisibility(View.GONE);
                mCenterLayout.setVisibility(View.GONE);
                break;
            case UI_MODE_SELECT:
                mLayoutBottom.setVisibility(View.VISIBLE);
                mTreasureView.setVisibility(View.VISIBLE);
                mCenterLayout.setVisibility(View.GONE);
                mHideTreasure.setVisibility(View.GONE);
                break;
            case UI_MODE_HIDE:
                if (mCurrentMarker != null) {
                    mCurrentMarker.setVisible(true);
                }
                mBaiduMap.hideInfoWindow();
                mCenterLayout.setVisibility(View.VISIBLE);
                mLayoutBottom.setVisibility(View.GONE);
                mBtnHideHere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLayoutBottom.setVisibility(View.VISIBLE);
                        mTreasureView.setVisibility(View.GONE);
                        mHideTreasure.setVisibility(View.VISIBLE);
                    }
                });
                break;
        }
    }

    public boolean onClickBack(){
        if (mUIMode!=UI_MODE_NORMAL){
            changeUiMode(UI_MODE_NORMAL);
            return false;
        }
        return true;
    }


    @Override
    public void showMessage(String msg) {
        mActivityUtils.showToast(msg);
    }

    @Override
    public void setData(List<Treasure> list) {

        for (Treasure treasure :
                list) {
            LatLng latLng = new LatLng(treasure.getLatitude(), treasure.getLongitude());
            addMarker(latLng, treasure.getId());
        }
    }
}
