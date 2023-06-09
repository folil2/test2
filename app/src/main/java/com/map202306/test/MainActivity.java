package com.map202306.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1981;
    private static final int REQUEST_CODE_LOCATION_SETTINGS = 2981;
    private static final String[] PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private LocationSettingsRequest mLocationSettingsRequest;
    private Location mLastLocation;

    private ImageButton myPositionButton;
    private TMapGpsManager tMapGpsManager;
    private TMapView tMapView;
    public List<Toilet> toiletList;
    private Bitmap resized;


    private EditText edtSearch;
    private ImageButton btnSearch;
    private RecyclerView rvSearch;

    private ImageButton SosButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayoutTmap = findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);
        tMapView.setCenterPoint(129.1627, 37.4534); // 강원대학교 좌표
        tMapView.setSKTMapApiKey("Ft6x7kExpN7fmYGCT0AzB1PtTvqg03cS5lcaN0GV");
        linearLayoutTmap.addView(tMapView);

        myPositionButton = findViewById(R.id.my_position);
        myPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocation();
            }
        });

        init();
        initLoadDB(); // initLoadDB() 메서드 호출 추가

            /*tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
                @Override
                public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                    return false;
                }

                @Override
                public boolean onPressUpEvent(ArrayList markerlist, ArrayList poilist, TMapPoint point, PointF pointf) {
                    // 클릭 이벤트 처리 코드
                    for (Object obj : markerlist) {
                        if (obj instanceof TMapMarkerItem) {
                            final TMapMarkerItem markerItem = (TMapMarkerItem) obj;
                            if (markerItem.getID().contains("marker_")) {
                                markerItem.setOnClickListener(new TMapView.OnClickListenerCallback() {
                                    @Override
                                    public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                                        // 마커 클릭 시 정보를 보여주는 로직
                                        // 마커에 해당하는 화장실 정보를 찾는다.
                                        Toilet toilet = null;
                                        for (Toilet t : toiletList) {
                                            if (("marker_" + t.getId()).equals(markerItem.getID())) {
                                                toilet = t;
                                                break;
                                            }
                                        }

                                        if (toilet != null) {
                                            // 화장실 정보를 이용하여 필요한 정보를 가져옵니다.
                                            String name = toilet.getName();
                                            String doro = toilet.getDoro();
                                            int man_so = toilet.getMan_so();
                                            int man_dae = toilet.getMan_dae();
                                            int man2_so = toilet.getMan2_so();
                                            int man2_dae = toilet.getMan2_dae();
                                            int woman = toilet.getWoman();
                                            int woman2 = toilet.getWoman2();
                                            double latitude = toilet.getLatitude();
                                            double longitude = toilet.getLongitude();


                                            // 정보를 보여주는 Activity로 전환하고 데이터를 전달한다
                                            Intent intent = new Intent(MainActivity.this, MapinfoActivity.class);
                                            intent.putExtra("address", name);
                                            intent.putExtra("name", doro);
                                            intent.putExtra("maleToiletCount", man_so);
                                            intent.putExtra("maleUrinalCount", man_dae);
                                            intent.putExtra("femaleToiletCount", man2_so);
                                            intent.putExtra("femaleUrinalCount", man2_dae);
                                            intent.putExtra("womanToiletCount", woman);
                                            intent.putExtra("womanUrinalCount", woman2);
                                            intent.putExtra("latitude", latitude);
                                            intent.putExtra("longitude", longitude);
                                            startActivity(intent);
                                        }

                                        return false;
                                    }

                                    @Override
                                    public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                                        return false;
                                    }
                                });
                            }
                        }
                    }

                    return false;
                }
            });*/
/*        SosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Sos.class);
                startActivity(intent);
            }
        });*/
        }




    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20 * 1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

        tMapGpsManager = new TMapGpsManager(this);
        tMapGpsManager.setMinTime(1000);
        tMapGpsManager.setMinDistance(5);
        tMapGpsManager.setProvider(TMapGpsManager.NETWORK_PROVIDER);
    }

    private void checkLocation() {
        if (isPermissionGranted()) {
            startLocationUpdates();
        } else {
            requestPermissions();
        }
    }

    private boolean isPermissionGranted() {
        int fineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED && coarseLocationPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (shouldProvideRationale) {
            // 사용자에게 권한 요청에 대한 설명이 필요한 경우, AlertDialog 등을 사용하여 설명을 제공할 수 있습니다.

            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ResolvableApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_CODE_LOCATION_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    sie.printStackTrace();
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Toast.makeText(MainActivity.this, "Location settings are inadequate, and cannot be fixed here. Fix in Settings.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    updateLocation(location);
                }
            }
        };
    }

    private void updateLocation(Location location) {
        mLastLocation = location;
        TMapPoint currentLocation = new TMapPoint(location.getLatitude(), location.getLongitude());
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());

        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        tMapView.setTrackingMode(true);

        TMapMarkerItem markerItem = new TMapMarkerItem();
        markerItem.setTMapPoint(currentLocation);

        // 고도에 따라 마커 크기 조절(맵 크기조절에 따라 마커도 조절)
        float elevation = (float) location.getAltitude();
        float markerSize = 0.5f + (elevation / 1000f);
        Bitmap originalMarker = BitmapFactory.decodeResource(getResources(), R.drawable.my_location);
        Bitmap scaledMarker = Bitmap.createScaledBitmap(originalMarker, (int) (originalMarker.getWidth() * markerSize), (int) (originalMarker.getHeight() * markerSize), false);
        markerItem.setIcon(scaledMarker);
        tMapView.setZoomLevel(15);//줌인

        tMapView.addMarkerItem("marker", markerItem);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        if (mLocationCallback != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOCATION_SETTINGS) {
            if (resultCode == RESULT_OK) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Location settings not satisfied, user canceled.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //마커 크기 조정
    //sql데이터베이스를 통해 화장실 위치 마커로 표시
    private void initLoadDB() {
        DataAdapter mDbHelper = new DataAdapter(getApplicationContext());
        try {
            mDbHelper.createDatabase();
            mDbHelper.open();

            // Retrieve toilet data from the database
            toiletList = mDbHelper.getTableData();

            // Add markers for each toilet location
            for (Toilet toilet : toiletList) {
                TMapMarkerItem markerItem = new TMapMarkerItem();
                TMapPoint toiletLocation = new TMapPoint(toilet.getLatitude(), toilet.getLongitude());
                markerItem.setTMapPoint(toiletLocation);

                // 고도에 따라 마커 크기 조절(맵 크기조절에 따라 마커도 조절)
                // Customize marker appearance if needed
                // For example:
                Bitmap markerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker_tour);
                // Bitmap scaledMarker = Bitmap.createScaledBitmap(markerBitmap, width, height, false);
                // markerItem.setIcon(scaledMarker);
                //마커 크기 조절
                markerBitmap = Bitmap.createScaledBitmap(markerBitmap, 50, 50, false);
                markerItem.setIcon(markerBitmap);
                tMapView.addMarkerItem("marker_" + toilet.getId(), markerItem);
            }

            mDbHelper.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //마커 클릭시 주소, 이름, 남성용 대변기수, 남성용 소변기수, 여성용 대변기수 정보 보여줌
    //onPressUpEvent() 함수가 호출이 됩니다.
    //해당 함수가 호출 될 때 markerlist로 마커의 List가 반환이 되고 해당 List와 조건문을 사용하여,
    //마커 클릭 이벤트를 구현하시면 될 것입니다.

    //markerlist: 클릭된 마커들의 목록
    //poilist: 클릭된 POI(Point of Interest)들의 목록
    //point: 클릭된 지점의 TMapPoint 객체
    //pointf: 클릭된 지점의 좌표 정보인 PointF 객체
/*    TMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
        @Override
        public boolean onPressUpEvent(ArrayList markerlist,ArrayList poilist, TMapPoint point, PointF pointf) {
            // 클릭 이벤트 처리 코드
        }
    });

    {
        @Override
        //public boolean onPressEvent(ArrayList markerlist,ArrayList poilist, TMapPoint point, PointF pointf) {
        public boolean onPressEvent (List < Toilet > toiletList,
        public List<Toilet> toiletList,final TMapPoint point, PointF pointf){
        // 마커 클릭 시 실행되는 로직을 여기에 작성합니다.
        for (final TMapMarkerItem markerItem : markerlist) {
            if (markerItem.getID().contains("marker_")) {
                markerItem.setOnClickListener(new TMapView.OnClickListenerCallback() {
                    @Override
                    public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, android.graphics.PointF pointF) {
                        // 마커 클릭 시 정보를 보여주는 로직을 여기에 작성합니다.
                        // 마커에 해당하는 화장실 정보를 찾습니다.
                        Toilet toilet = null;
                        for (Toilet t : toiletList) {
                            if (("marker_" + t.getId()).equals(markerItem.getID())) {
                                toilet = t;
                                break;
                            }
                        }

                        if (toilet != null) {
                            // 화장실 정보를 이용하여 필요한 정보를 가져옵니다.
                            String name = toilet.getName();
                            String doro = toilet.getDoro();
                            int man_so = toilet.getMan_so();
                            int man_dae = toilet.getMan_dae();
                            int man2_so = toilet.getMan2_so();
                            int man2_dae = toilet.getMan2_dae();
                            int woman = toilet.getWoman();
                            int woman2 = toilet.getWoman2();
                            double latitude = toilet.getLatitude();
                            double longitude = toilet.getLongitude();


                            // 정보를 보여주는 Activity로 전환하고 데이터를 전달한다
                            Intent intent = new Intent(MainActivity.this, MapinfoActivity.class);
                            intent.putExtra("address", name);
                            intent.putExtra("name", doro);
                            intent.putExtra("maleToiletCount", man_so);
                            intent.putExtra("maleUrinalCount", man_dae);
                            intent.putExtra("femaleToiletCount", man2_so);
                            intent.putExtra("femaleToiletCount", man2_dae);
                            intent.putExtra("femaleToiletCount", woman);
                            intent.putExtra("femaleToiletCount", woman2);
                            intent.putExtra("femaleToiletCount", latitude);
                            intent.putExtra("femaleToiletCount", longitude);
                            startActivity(intent);
                        }

                        return false;
                    }
                });
            }
        }

    }
    });*/


}


