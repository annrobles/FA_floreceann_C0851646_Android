package com.example.product.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.product.databinding.ActivityListProductBinding;
import com.example.product.model.Product;
import com.example.product.viewmodel.ProductViewModel;
import com.example.product.viewmodel.ProductViewModelFactory;


public class ProductListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = ProductListActivity.class.getName();

    ActivityListProductBinding binding;

    private List<Product> products = new ArrayList<>();
    private ProductAdapter productAdapter;

    private ProductViewModel productViewModel;

    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Log.i(TAG, "onActivityResult");

                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        Intent data = result.getData();
                        String name = data.getStringExtra("name");
                        String description = data.getStringExtra("description");
                        Double price = Double.parseDouble(data.getStringExtra("price"));
                        Double latitude = 40.124;
                        Double longitude = 79.123;

                        Product product = new Product(name, description, price, latitude, longitude);
                        Log.i("Contact Result:", product.toString());
                        productViewModel.saveProduct(product);
                    }
                } else if (result.getResultCode() == 5) {
                    if (result.getData() != null) {
                        Product product = (Product) result.getData().getSerializableExtra("product");
                        productViewModel.deleteProduct(product);
                    }
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityListProductBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.addProductBtn.setOnClickListener(this::createNewProduct);

        RecyclerView recyclerView = binding.productList;
        productViewModel = new ViewModelProvider(this, new ProductViewModelFactory(getApplicationContext())).get(ProductViewModel.class);

        binding.search.setOnQueryTextListener(this);

        productViewModel.getProductLiveData().observe(this, productResult -> {
            if (productResult != null) {
                products = productResult;
                productAdapter = new ProductAdapter(products);

                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(productAdapter);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        productAdapter.notifyItemChanged(products.size());
    }

    private void createNewProduct(View view) {

        Intent newProductIntent = new Intent(this, ProductEntryActivity.class);
        newProductIntent.putExtra("updateBtn", false);
        newProductIntent.putExtra("deleteBtn", false);
        launcher.launch(newProductIntent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        productAdapter.filter(newText);
        return false;
    }
}
