package com.example.product.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import com.example.product.dao.ProductDao;
import com.example.product.db.ProductRoom;
import com.example.product.model.Product;

public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(Context context) {
        ProductRoom productRoom = ProductRoom.getDatabase(context);
        productDao = productRoom.productDao();
    }

    public LiveData<List<Product>> fetchProducts() {
        return productDao.fetchAll();
    }

    public void save(Product product) {
        ProductRoom.databaseWriterExecutor.execute(() -> productDao.save(product));
    }

    public void delete(Product product) {
        ProductRoom.databaseWriterExecutor.execute(() -> this.productDao.delete(product));
    }

    public void update(Product product) {
        ProductRoom.databaseWriterExecutor.execute(() -> this.productDao.update(product));
    }
}
