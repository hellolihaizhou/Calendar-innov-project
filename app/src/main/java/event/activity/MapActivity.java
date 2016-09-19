package event.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.BDNotifyListener;//假如用到位置提醒功能，需要import该类
import com.baidu.location.Poi;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lihaizhou.mycalendar.R;

import database.bean.addressBean;


public class MapActivity extends Activity {
    MapView mMapView = null;
    BaiduMap mBaiduMap = null;
    BitmapDescriptor mCurrentMarker;
    LocationClient mLocClient;
    public LocationClient mLocationClient = null;
    Button requestLocButton;
    private LocationMode mCurrentMode;
    boolean isFirstLoc = true; // 是否首次定位
    private BitmapDescriptor bitmap;
    private String address= "";
    private double latitude;
    private double longitude;
    private int requestCode;
    public BDLocationListener myListener = new MyLocationListenner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        Intent intent=new Intent();
        requestCode=intent.getIntExtra("requestCode", -1);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        mCurrentMode = LocationMode.NORMAL;
        mBaiduMap = mMapView.getMap();

        // 设置marker图标
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.maker);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            //此方法就是点击地图监听
            @Override
            public void onMapClick(LatLng latLng) {
                //获取经纬度
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                //先清除图层
                mBaiduMap.clear();
                // 定义Maker坐标点
                LatLng point = new LatLng(latitude, longitude);
                // 构建MarkerOption，用于在地图上添加Marker
                MarkerOptions options = new MarkerOptions().position(point)
                        .icon(bitmap);
                // 在地图上添加Marker，并显示
                mBaiduMap.addOverlay(options);
                //实例化一个地理编码查询对象
                GeoCoder geoCoder = GeoCoder.newInstance();
                //设置反地理编码位置坐标
                ReverseGeoCodeOption op = new ReverseGeoCodeOption();
                op.location(latLng);
                //发起反地理编码请求(经纬度->地址信息)
                geoCoder.reverseGeoCode(op);
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
                        //获取点击的坐标地址
                        address = arg0.getAddress();
                        //  System.out.println("地址----》"+ arg0.getAddress());
  /*                      Intent intent = new Intent();
                        intent.putExtra("latitude",arg0.getLocation().latitude);
                        intent.putExtra("longitude",arg0.getLocation().longitude);
                        intent.putExtra("address",address);
                        setResult(RESULT_OK,intent);*/
                        addressBean aBean=new addressBean(latitude, longitude, address);
                        Intent intent=getIntent();
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("addressBean", aBean);;
                        intent.putExtras(bundle);
                        setResult(requestCode, intent);
                    }

                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult arg0) {
                    }
                });
            }
        });

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLocationClient != null)
        {
            mLocationClient.stop();
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }



    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {

                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
/*        if(mLocationClient != null)
        {
            mLocationClient.stop();
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();*/
        super.finish();
    }
}
