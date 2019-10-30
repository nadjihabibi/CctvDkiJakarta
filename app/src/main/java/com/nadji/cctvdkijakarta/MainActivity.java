package com.nadji.cctvdkijakarta;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    LatLng center, latLng;
    String link;

    private ClusterManager<MarkerClusterItem> clusterManager;

    public static final String LINK = "url";
    public static final String LAT = "latitude";
    public static final String LNG = "longitude";

    private String KEY_ALAMAT = "ALAMAT";

    private static final String url = "http://api.jakarta.go.id/v1/cctvbalitower/?format=geojson";
//    private static final String url = "https://api.jsonbin.io/b/5d1da605f467d60d75ad0c1f/1";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        // Mengarahkan map ke Monas
        center = new LatLng(-6.175147, 106.827142);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 15.0f));

        getMarkers();
        clusterManager = new ClusterManager<>(this, googleMap);

        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
        googleMap.setOnInfoWindowClickListener(clusterManager);

//        InfoWindowAdapter markerInfoWindowAdapter = new InfoWindowAdapter(getApplicationContext());
//        googleMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
        //clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new InfoWindowAdapter());

//        googleMap.setOnInfoWindowClickListener(clusterManager);
        setClusterManagerClickListener();
    }

    //methode click pada marker
    private void setClusterManagerClickListener() {

        //methode clik pada kelompok marker
        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MarkerClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<MarkerClusterItem> cluster) {
                Collection<MarkerClusterItem> listItems = cluster.getItems();
                final List<String> listAlamat = new ArrayList<>();
                final List<String> listAlamat2 = new ArrayList<>();
                for (MarkerClusterItem item : listItems) {
                    String linkcamera = item.getTitle();

                    //..................Menampilkan Listview dialog di marker...........................
//                listNames.add(item.getLat()+","+ item.getLng());
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(Double.valueOf(item.getPosition().latitude), Double.valueOf(item.getPosition().longitude), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String address = addresses.get(0).getAddressLine(0);
                    listAlamat.add(address);
                    listAlamat2.add(linkcamera);
                }
                ArrayList<HashMap<String, List<String>>> listt = new ArrayList<>();
                HashMap<String, List<String>> hashMap = new HashMap<>();
                hashMap.put("alamatt", listAlamat);
                hashMap.put("linkk", listAlamat2);
                listt.add(hashMap);
//                Log.e("cetak listt: ", String.valueOf(hashMap));
                gMap.animateCamera(CameraUpdateFactory.newLatLng(cluster.getPosition()), new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        ListViewDialog listViewDialog = new ListViewDialog(MainActivity.this, listt);
                        listViewDialog.showDialog();
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                return true;
            }
        });

        //methode clik pada satu marker
        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerClusterItem>() {
            @Override
            public boolean onClusterItemClick(MarkerClusterItem item) {
                clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new CustomInfoViewAdapter(LayoutInflater.from(getApplicationContext())));
                gMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
//                Log.e("kie", "contohe klik marker tunggal");
                return false;
            }
        });

        //methode clik pada info window marker
        clusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<MarkerClusterItem>() {
            @Override
            public void onClusterItemInfoWindowClick(MarkerClusterItem markerClusterItem) {
                String item = markerClusterItem.getTitle();
//                Log.e("kie", "contohe klik info window" + item);
                Intent intent = new Intent(MainActivity.this, PlayStream.class);
                intent.putExtra(KEY_ALAMAT, item);
                startActivity(intent);
            }
        });
    }

    // Fungsi get JSON marker
    private void getMarkers() {
        LottieAnimationView lottieLoading = findViewById(R.id.loading);
        LottieAnimationView lottieNetworkError = findViewById(R.id.networkError);
        lottieLoading.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                lottieLoading.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    String getObject = jObj.getString("features");
                    JSONArray jsonArray = new JSONArray(getObject);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject properties = jsonObject.getJSONObject("properties");
                        JSONObject lokasi = properties.getJSONObject("location");
                        link = properties.getString(LINK);
                        latLng = new LatLng(Double.parseDouble(lokasi.getString(LAT)), Double.parseDouble(lokasi.getString(LNG)));
                        // Menambah data marker yang tercluster untuk di tampilkan ke google map
                        MarkerClusterItem offsetItem = new MarkerClusterItem(latLng, link);
                        clusterManager.addItem(offsetItem);
                        // cetak respon di terminal
//                        Log.e("Response: ", link);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, error -> {
            lottieLoading.setVisibility(View.INVISIBLE);
            String message = null;
            if (error instanceof NetworkError) {
                lottieNetworkError.setVisibility(View.VISIBLE);
                message = "Cannot connect to Internet...Please check your connection!";
            } else if (error instanceof ServerError) {
                message = "The server could not be found. Please try again after some time!!";
            } else if (error instanceof AuthFailureError) {
                message = "Cannot connect to Internet...Please check your connection!";
            } else if (error instanceof ParseError) {
                message = "Parsing error! Please try again after some time!!";
            } else if (error instanceof NoConnectionError) {
                message = "Cannot connect to Internet...Please check your connection!";
            } else if (error instanceof TimeoutError) {
                message = "Connection TimeOut! Please check your internet connection.";
            }
            Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("authorization", getString(R.string.autorization_key));
                //return super.getHeaders();
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    /**
     //.....................................HTTP Request tanpa volley....................................

     private class DownloadGeoJsonFile extends AsyncTask<String, Void, JSONObject> {
    @Override protected JSONObject doInBackground(String... response) {
    try {
    URL url = new URL("http://api.jakarta.go.id/v1/cctvbalitower/?format=geojson");
    //                URL url = new URL("https://api.jsonbin.io/b/5d1da605f467d60d75ad0c1f/1");
    URLConnection conn = url.openConnection();
    conn.setRequestProperty("authorization", "J4cMsZFdpYnK2R/gE6uDKA5iH1Ldj9bbs8qVGWF90VsooOkPQVr1j1XlNDzrWrbW4iMI5m/0ZDEX1pYI0w+hsvpIy/697QTmWJ/POR1q6tk=");
    conn.connect();

    InputStream stream = conn.getInputStream();

    String line;
    StringBuilder result = new StringBuilder();
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

    while ((line = reader.readLine()) != null) {
    // Read and save each line of the stream
    result.append(line);
    }
    reader.close();
    stream.close();

    try {
    //ambil json objec dari resul
    JSONObject jObj = new JSONObject(String.valueOf(result));
    // ambil json objec features
    String getFeatures = jObj.getString("features");
    //simpan json objec feature ke array
    JSONArray jsonArray = new JSONArray(getFeatures);

    for (int i = 0; i < jsonArray.length(); i++) {
    JSONObject features = jsonArray.getJSONObject(i);
    JSONObject properties = features.getJSONObject("properties");
    JSONObject lokasi = properties.getJSONObject("location");
    String latt = lokasi.getString("latitude");
    String longg = lokasi.getString("longitude");

    latLng = new LatLng(Double.parseDouble(lokasi.getString(LAT)), Double.parseDouble(lokasi.getString(LNG)));


    Log.i("Tag", "kie respone " + latLng.toString());
    addMarker(latLng);
    }
    } catch (JSONException e) {
    e.printStackTrace();
    }
    } catch (MalformedURLException e) {
    e.printStackTrace();
    } catch (IOException e) {
    e.printStackTrace();
    }
    return null;
    }
    }

     //.........................................HTTP Request tanpa volley....................................
     */
}

