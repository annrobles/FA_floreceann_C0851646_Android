package com.example.product.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.product.databinding.ActivityProductEntryBinding;
import com.example.product.model.Product;
import com.example.product.viewmodel.ProductViewModel;
import com.example.product.viewmodel.ProductViewModelFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ProductEntryActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final String TAG = "ProductEntryActivity";

    private ActivityProductEntryBinding binding;

    private Product product;
    Double latitude = 43.6426;
    Double longitude = -79.3871;

    LocationManager locationManager;
    LocationListener locationListener;

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductEntryBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.saveBtn.setOnClickListener(this::saveProduct);
        binding.deleteBtn.setOnClickListener(this::deleteProduct);
        binding.updateBtn.setOnClickListener(this::updateProduct);
        binding.showMapBtn.setOnClickListener(this::showMap);

        Intent intent = getIntent();

        product = (Product) intent.getSerializableExtra("product");

        binding.saveBtn.setVisibility(!intent.getBooleanExtra("saveBtn", true) ? View.INVISIBLE : View.VISIBLE);

        binding.deleteBtn.setVisibility(!intent.getBooleanExtra("deleteBtn", true) ? View.INVISIBLE : View.VISIBLE);
        binding.updateBtn.setVisibility(!intent.getBooleanExtra("updateBtn", true) ? View.INVISIBLE : View.VISIBLE);
        binding.showMapBtn.setVisibility(!intent.getBooleanExtra("showMapBtn", true) ? View.INVISIBLE : View.VISIBLE);

        if (product != null) {
            binding.nameTxt.setText(product.getName());
            binding.descriptionTxt.setText(product.getDescription());
            binding.priceTxt.setText(product.getPrice().toString());
            latitude = product.getLatitude();
            longitude = product.getLongitude();
        }
        else {
            locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    Log.i(TAG, "onLocationChanged: " + location);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            };

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lasKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lasKnownLocation != null) {
                    latitude = lasKnownLocation.getLatitude();
                    longitude = lasKnownLocation.getLongitude();
                }
            }
        }
    }

    private void saveProduct(View view) {
        Intent resultProduct = new Intent();

        resultProduct.putExtra("name", Objects.requireNonNull(binding.nameTxt.getText()).toString());
        resultProduct.putExtra("description", Objects.requireNonNull(binding.descriptionTxt.getText()).toString());
        resultProduct.putExtra("price", Objects.requireNonNull(binding.priceTxt.getText()).toString());
        resultProduct.putExtra("latitude", latitude.toString());
        resultProduct.putExtra("longitude", longitude.toString());
        setResult(Activity.RESULT_OK, resultProduct);

        finish();
    }

    private void updateProduct(View view) {
        ProductViewModel productViewModel = new ViewModelProvider(this, new ProductViewModelFactory(getApplicationContext())).get(ProductViewModel.class);

        if (!Objects.requireNonNull(binding.nameTxt.getText()).toString().equals("")) {
            product.setName(binding.nameTxt.getText().toString());
        }

        if (!Objects.requireNonNull(binding.descriptionTxt.getText()).toString().equals("")) {
            product.setDescription(binding.descriptionTxt.getText().toString());
        }

        if (!Objects.requireNonNull(binding.priceTxt.getText()).toString().equals("")) {
            product.setPrice(Double.parseDouble(binding.priceTxt.getText().toString()));
        }

        productViewModel.updateProduct(product);
        finish();
    }

    private void deleteProduct(View view) {

        ProductViewModel productViewModel = new ViewModelProvider(this, new ProductViewModelFactory(getApplicationContext())).get(ProductViewModel.class);
        productViewModel.deleteProduct(product);
        finish();
    }

    private  void showMap(View view) {
        Intent mapIntent = new Intent(ProductEntryActivity.this, ShowMapActivity.class);
        mapIntent.putExtra("latitude", latitude.toString());
        mapIntent.putExtra("longitude", longitude.toString());
        view.getContext().startActivity(mapIntent);
    }
}
