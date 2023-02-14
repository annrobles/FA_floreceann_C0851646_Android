package com.example.product.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.product.model.Product;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.product.dao.ProductDao;
import com.example.product.model.Product;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class ProductRoom extends RoomDatabase {

    private static volatile ProductRoom INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract ProductDao productDao();

    public static ProductRoom getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (ProductRoom.class) {
                INSTANCE = Room.databaseBuilder(context, ProductRoom.class, "product_db").build();
            }
        }
        return INSTANCE;
    }
}
