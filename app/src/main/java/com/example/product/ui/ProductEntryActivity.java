package com.example.product.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import com.example.product.databinding.ActivityProductEntryBinding;
import com.example.product.model.Product;
import com.example.product.viewmodel.ProductViewModel;
import com.example.product.viewmodel.ProductViewModelFactory;

public class ProductEntryActivity extends AppCompatActivity {

    private ActivityProductEntryBinding binding;

    private Product product;

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

        Intent intent = getIntent();

        product = (Product) intent.getSerializableExtra("product");

        binding.saveBtn.setVisibility(!intent.getBooleanExtra("saveBtn", true) ? View.INVISIBLE : View.VISIBLE);

        binding.deleteBtn.setVisibility(!intent.getBooleanExtra("deleteBtn", true) ? View.INVISIBLE : View.VISIBLE);
        binding.updateBtn.setVisibility(!intent.getBooleanExtra("updateBtn", true) ? View.INVISIBLE : View.VISIBLE);

        if (product != null) {
            binding.nameTxt.setText(product.getName());
            binding.descriptionTxt.setText(product.getDescription());
            binding.priceTxt.setText(product.getPrice().toString());
        }
    }

    private void saveProduct(View view) {
        Intent resultProduct = new Intent();

        resultProduct.putExtra("name", Objects.requireNonNull(binding.nameTxt.getText()).toString());
        resultProduct.putExtra("description", Objects.requireNonNull(binding.descriptionTxt.getText()).toString());
        resultProduct.putExtra("price", Objects.requireNonNull(binding.priceTxt.getText()).toString());
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
}