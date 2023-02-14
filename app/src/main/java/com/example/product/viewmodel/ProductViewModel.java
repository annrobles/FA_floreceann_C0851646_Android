package com.example.product.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;

public class ProductViewModel extends ViewModel {
    private final ProductRepository repository;
    private LiveData<List<Product>> productLiveData;

    public ProductViewModel(Context context) {
        repository = new ProductRepository(context);

        productLiveData = new MutableLiveData<>();

    }

    public void saveProduct(Product product) {
        repository.save(product);
    }


    public LiveData<List<Product>> getProductLiveData() {
        productLiveData = repository.fetchProducts();
        return productLiveData;
    }

    public void deleteProduct(Product product) {
        repository.delete(product);
    }

    public void updateProduct(Product product) {
        repository.update(product);
    }
}
