package fpt.edu.gr2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class activity_maps extends FragmentActivity implements OnMapReadyCallback {
     GoogleMap mMap ;
     private Spinner spinner_google_map ;
     private ImageButton btn_back_home ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_maps);

        // Nhận SupportMapFragment và nhận thông báo khi bản đồ sẵn sàng được sử dụng.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        btn_back_home = findViewById(R.id.btn_back_home);
        btn_back_home.setOnClickListener(view -> {
            Intent intent = new Intent(activity_maps.this, activity_home.class);
            startActivity(intent);
            finish();
        });
          addIterm();
          addEvents();

    }

    private void addEvents() {
        spinner_google_map.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case 1:
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case 2:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 3:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private  void addIterm(){
       spinner_google_map = findViewById(R.id.spinner_google_map);

       ArrayList<String> list = new ArrayList<>();
       list.add("Hybrid");
       list.add("Terain");
       list.add("Satellite");
       list.add("Normal");
       ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,list);
       spinner_google_map.setAdapter(arrayAdapter);



   }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
      mMap = googleMap ;
      mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Existing marker in FPT University
        LatLng FPTU = new LatLng(10.013162219620174, 105.73222136386758);
        mMap.addMarker(new MarkerOptions().position(FPTU).title("FPTU Can Tho"));

        LatLng fuQuyNhon = new LatLng(13.804124022705652, 109.21908102243833);
        mMap.addMarker(new MarkerOptions().position(fuQuyNhon).title("FPTU Quy Nhon"));

        LatLng fuHoaLac = new LatLng(21.065672470902758, 105.52512140954303);
        mMap.addMarker(new MarkerOptions().position(fuHoaLac).title("FPTU Hoa Lac"));

        LatLng fuHCM = new LatLng(10.862165182269598, 106.81041577722421);
        mMap.addMarker(new MarkerOptions().position(fuHCM).title("FPTU HCM"));



        // Other map settings
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(FPTU, 20));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);

        // Permissions and location settings
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Consider calling ActivityCompat#requestPermissions
            return;
        }
        mMap.setMyLocationEnabled(true);


    }
}