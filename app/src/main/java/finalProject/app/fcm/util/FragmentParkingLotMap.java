package finalProject.app.fcm.util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import finalProject.app.fcm.R;
import finalProject.app.fcm.SubActivity;

public class FragmentParkingLotMap extends Fragment implements MapView.POIItemEventListener {
    ViewGroup mapViewContainer;
    MapView mapView;
    ViewGroup rootView;
    GpsTracker gpsTracker;
    double latitude;
    double longitude;
    MapPOIItem customMarker;


    public static FragmentParkingLotMap newInstance(int number) {
        FragmentParkingLotMap parkingLotMap = new FragmentParkingLotMap();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        parkingLotMap.setArguments(bundle);
        return parkingLotMap;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int num = getArguments().getInt("number");
        }
        Log.d("why", "map onCreate");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        if(inflater!=null) {
            rootView = (ViewGroup)LayoutInflater.from(inflater.getContext()).inflate(R.layout.fragment_main_parking_lot_map, container, false);
            Log.d("why", "map onCreateView");
            return rootView;
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("why", "map onResume");
        if(getActivity()!=null){
            /* MapView */
            mapView = new MapView(getActivity());
            mapViewContainer = rootView.findViewById(R.id.map_view);
            mapViewContainer.addView(mapView);
            mapView.setPOIItemEventListener(this);
            ImageView currentLocation = rootView.findViewById(R.id.currentLocation);
            ImageView zoomIn = rootView.findViewById(R.id.zoomIn);
            ImageView zoomOut = rootView.findViewById(R.id.zoomOut);
            Spinner selectMarker = rootView.findViewById(R.id.mapSelectMarker);
            String[] item = {"주차장","A","B","C","D","E","F","G","H"};
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, item);
            selectMarker.setAdapter(spinnerAdapter);
            /* Current location */
            gpsTracker = new GpsTracker(getActivity());
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.d("fragment","main Map: latitude:"+latitude+", longitude:"+longitude);
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
            /* Current location Marker */
            customMarker = new MapPOIItem();
            customMarker.setItemName("현재 위치");
            customMarker.setTag(1);
            customMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
            customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            customMarker.setCustomImageResourceId(R.drawable.panda);
            customMarker.setCustomImageAutoscale(false);
            customMarker.setCustomImageAnchor(0.5f, 1.0f);
            mapView.addPOIItem(customMarker);
            /* Parking Lot Marker Info */
            /* A */ String Name_A = "A"; double latitude_A = 37.5098974029728; double longitude_A = 127.05530304646592;
            /* F */ String Name_B = "B"; double latitude_B = 37.511156760440166; double longitude_B = 127.05617489831415;
            /* H */ String Name_C = "C"; double latitude_C = 37.51009680074465; double longitude_C = 127.0526719134884;
            /* B */ String Name_D = "D"; double latitude_D = 37.508043903245984; double longitude_D = 127.0579305865121;
            /* G */ String Name_E = "E"; double latitude_E = 37.50721014314015; double longitude_E = 127.05395051959206;
            /* D */ String Name_F = "F"; double latitude_F = 37.51233224600035; double longitude_F = 127.05325498539442;
            /* C */ String Name_G = "G"; double latitude_G = 37.50659031553419; double longitude_G = 127.057243674977;
            /* E */ String Name_H = "H"; double latitude_H = 37.50865116993856; double longitude_H = 127.05939784939036;
            /* add Parking Lot Marker */
            mapView.addPOIItem(addParkingLot(Name_A,latitude_A,longitude_A));
            mapView.addPOIItem(addParkingLot(Name_B,latitude_B,longitude_B));
            mapView.addPOIItem(addParkingLot(Name_C,latitude_C,longitude_C));
            mapView.addPOIItem(addParkingLot(Name_D,latitude_D,longitude_D));
            mapView.addPOIItem(addParkingLot(Name_E,latitude_E,longitude_E));
            mapView.addPOIItem(addParkingLot(Name_F,latitude_F,longitude_F));
            mapView.addPOIItem(addParkingLot(Name_G,latitude_G,longitude_G));
            mapView.addPOIItem(addParkingLot(Name_H,latitude_H,longitude_H));
            currentLocation.setOnClickListener(view -> {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
            });
            zoomIn.setOnClickListener(view -> {
                Log.d("map","Zoom In:"+mapView.getZoomLevel()+"=>"+(mapView.getZoomLevel()+1));
                mapView.setZoomLevel((mapView.getZoomLevel()+1),true);
            });
            zoomOut.setOnClickListener(view -> {
                Log.d("map","Zoom Out:"+mapView.getZoomLevel()+"=>"+(mapView.getZoomLevel()-1));
                mapView.setZoomLevel((mapView.getZoomLevel()-1),true);
            });
            selectMarker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i){
                        case 1: mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude_A, longitude_A), true);
                            break;
                        case 2: mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude_B, longitude_B), true);
                            break;
                        case 3: mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude_C, longitude_C), true);
                            break;
                        case 4: mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude_D, longitude_D), true);
                            break;
                        case 5: mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude_E, longitude_E), true);
                            break;
                        case 6: mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude_F, longitude_F), true);
                            break;
                        case 7: mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude_G, longitude_G), true);
                            break;
                        case 8: mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude_H, longitude_H), true);
                            break;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }
    public MapPOIItem addParkingLot(String name, double latitude, double longitude){
        MapPOIItem customMarker2 = new MapPOIItem();
        customMarker2.setItemName(name);
        customMarker2.setTag(2);
        customMarker2.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
        customMarker2.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        customMarker2.setCustomImageResourceId(R.drawable.pmarker);
        customMarker2.setCustomImageAutoscale(false);
        customMarker2.setCustomImageAnchor(0.5f, 1.0f);
        return customMarker2;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("why", "map onPause");
        mapViewContainer.removeView(mapView);
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        if(mapPOIItem.getTag()==2){
            String p_id = mapPOIItem.getItemName();
            Intent intent = new Intent(getActivity(), SubActivity.class);
            intent.putExtra("p_id", p_id);
            startActivity(intent);
            Log.d("why", "onCalloutBalloonOfPOIItemTouched");
        }
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }
}
