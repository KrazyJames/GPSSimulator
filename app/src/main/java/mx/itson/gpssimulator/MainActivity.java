package mx.itson.gpssimulator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.List;

import mx.itson.gpssimulator.WebService.Interfaces.LocationJsonAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationClient;
    Retrofit retrofit;
    TextView txtVista;
    Button btnGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.17/potrobus/public/location/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        txtVista = findViewById(R.id.txtVista);
        btnGet = findViewById(R.id.btnGet);

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocations();
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                fusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            System.out.println("Location gotten");
                            insertLocation(location.getLatitude(), location.getLongitude());
                            System.out.println("Message sent, waiting for response from server...");
                            Toast.makeText(getApplicationContext(), location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void getLocations(){
        LocationJsonAPI locationJsonAPI = retrofit.create(LocationJsonAPI.class);
        Call<List<mx.itson.gpssimulator.Entidades.Location>> call = locationJsonAPI.getLocations();
        call.enqueue(new Callback<List<mx.itson.gpssimulator.Entidades.Location>>() {
            @Override
            public void onResponse(Call<List<mx.itson.gpssimulator.Entidades.Location>> call, Response<List<mx.itson.gpssimulator.Entidades.Location>> response) {
                if(!response.isSuccessful()){
                    txtVista.setText("code: "+response.code());
                    return;
                }
                txtVista.setText("");
                List<mx.itson.gpssimulator.Entidades.Location> locations = response.body();
                for(mx.itson.gpssimulator.Entidades.Location l : locations){
                    String content = new Gson().toJson(l);
                    txtVista.append(content+"\n");
                }
            }

            @Override
            public void onFailure(Call<List<mx.itson.gpssimulator.Entidades.Location>> call, Throwable t) {
                txtVista.setText(t.getMessage());
            }
        });
    }

    private void insertLocation(double lat, double lng){
        mx.itson.gpssimulator.Entidades.Location lastLocation = new mx.itson.gpssimulator.Entidades.Location(lat, lng);
        LocationJsonAPI locationJsonAPI = retrofit.create(LocationJsonAPI.class);
        Call<mx.itson.gpssimulator.Entidades.Location> call = locationJsonAPI.postLocation(lastLocation);
        call.enqueue(new Callback<mx.itson.gpssimulator.Entidades.Location>() {
            @Override
            public void onResponse(Call<mx.itson.gpssimulator.Entidades.Location> call, Response<mx.itson.gpssimulator.Entidades.Location> response) {
                if (!response.isSuccessful()){
                    System.out.println("Code"+response.code());
                    return;
                }
                System.out.println("New item added to server: "+response.body().getId());
            }

            @Override
            public void onFailure(Call<mx.itson.gpssimulator.Entidades.Location> call, Throwable t) {
                System.out.println("Failed: " + t.getMessage());
            }
        });
    }
}
