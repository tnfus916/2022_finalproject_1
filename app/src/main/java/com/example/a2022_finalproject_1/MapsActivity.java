package com.example.a2022_finalproject_1;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.a2022_finalproject_1.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private Double longitude;
    private Double latitude;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // 지도가 서울에서 뜨게하기
        LatLng SEOUL = new LatLng(37.56, 126.99);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));  //초기 보여주기 위치설정
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));  // 줌의 정도
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);    // 지도 유형 설정

        //맵에서 클릭하는 위치에 마커 생성
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();   //마커 생성
                latitude = point.latitude; // 위도
                longitude = point.longitude; // 경도
                mOptions.position(new LatLng(latitude, longitude)); // 클릭한 위치에 마커 생성
                address= getAddress(latitude,longitude);
                mOptions.title("식사 장소");    // 마커 제목
                mOptions.snippet(address);    //마커설명

                mMap.addMarker(mOptions);   //마커 핀 추가(삭제 시 오류)

                //마커가 여러개 생기면 그 전에 생성된 걸 삭제하거나 처음부터 하나만 마크될 수 있도록 설정해야함
                //코드 추가
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.getTitle();
                marker.getSnippet();
//                String text = "latitude =" + marker.getPosition().latitude + ", longitude =" + marker.getPosition().longitude;
//                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                return false;
            }
        });

        Button btnMap=(Button) findViewById(R.id.btnGetAdrs);
        btnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),address, Toast.LENGTH_LONG).show();

                Intent intent1 = new Intent(MapsActivity.this,writeActivity.class);
                intent1.putExtra("주소",address);
                startActivity(intent1);
            }

        });
    }
    public String getAddress(double latitude, double longitude){
        String nowAddress="현재 위치의 주소를 확인할 수 없습니다.";
        Geocoder geocoder=new Geocoder(this);
        List<Address> address;
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(latitude, longitude, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress  = currentLocationAddress;

                }
            }

        } catch (IOException e) {
            Toast.makeText(MapsActivity.this,"현재 위치의 주소를 가져 올 수 없습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show();
            e.printStackTrace();    //에러 메세지의 근원지를 찾아서 단계별로 에러를 출력
        }
        return nowAddress;
    }
}