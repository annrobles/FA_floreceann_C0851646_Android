package com.example.product.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.product.R;
import com.example.product.databinding.ActivityListProductBinding;
import com.example.product.helper.SwipeHelper;
import com.example.product.model.Product;
import com.example.product.viewmodel.ProductViewModel;
import com.example.product.viewmodel.ProductViewModelFactory;

import java.util.ArrayList;
import java.util.List;


public class ProductListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = ProductListActivity.class.getName();

    ActivityListProductBinding binding;

    private List<Product> products = new ArrayList<>();
    private ProductAdapter productAdapter;

    private ProductViewModel productViewModel;

    private SwipeHelper swipeHelper;

    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Log.i(TAG, "onActivityResult");

                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        Intent data = result.getData();
                        String name = data.getStringExtra("name");
                        String description = data.getStringExtra("description");
                        Double price = Double.parseDouble(data.getStringExtra("price"));
                        Double latitude = Double.parseDouble(data.getStringExtra("latitude"));
                        Double longitude = Double.parseDouble(data.getStringExtra("longitude"));

                        Product product = new Product(name, description, price, latitude, longitude);
                        Log.i("Product Result:", product.toString());
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
                binding.productCount.setText("Product count: " + products.size());
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(productAdapter);
            }
        });

        swipeHelper = new SwipeHelper(this, 300, binding.productList) {
            @Override
            protected void instantiateSwipeButton(RecyclerView.ViewHolder viewHolder, List<SwipeUnderlayButton> buffer) {
                buffer.add(new SwipeUnderlayButton(ProductListActivity.this,
                        "Delete",
                        R.drawable.ic_delete_white,
                        30,
                        50,
                        Color.parseColor("#ff3c30"),
                        SwipeDirection.LEFT,
                        position -> {
                            Product product = products.get(position);
                            productViewModel.deleteProduct(product);
                            productAdapter.notifyItemRemoved(position);
                        }));
                buffer.add(new SwipeUnderlayButton(ProductListActivity.this,
                        "Update",
                        R.drawable.ic_update_white,
                        30,
                        50,
                        Color.parseColor("#ff9502"),
                        SwipeDirection.LEFT,
                        position -> {
                            Intent productDetailIntent = new Intent(ProductListActivity.this, ProductEntryActivity.class);
                            productDetailIntent.putExtra("product", products.get(position));
                            productDetailIntent.putExtra("saveBtn", false);
                            startActivity(productDetailIntent);
                        }));
            }
        };
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
        newProductIntent.putExtra("showMapBtn", false);
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
