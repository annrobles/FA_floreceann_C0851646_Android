package com.example.product.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.product.model.Product;

@Dao
public interface ProductDao {
    @Insert
    void save(Product product);

    @Query("SELECT * FROM Product ORDER BY id")
    LiveData<List<Product>> fetchAll();

    @Delete
    void delete(Product product);

    @Query("DELETE FROM Product WHERE id = :id")
    void deleteById(Long id);

    @Update
    void update(Product product);

    @Insert
    void insertAll(Product product[]);
}
