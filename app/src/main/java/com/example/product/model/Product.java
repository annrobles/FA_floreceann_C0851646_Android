package com.example.product.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Product implements Serializable {
    @PrimaryKey
    private Long id;
    @ColumnInfo(name = "Name")
    private String name;
    @ColumnInfo(name = "Description")
    private String description;
    @ColumnInfo(name = "Price")
    private Double price;

    @ColumnInfo(name = "Latitude")
    private Double latitude;
    @ColumnInfo(name = "Longitude")
    private Double longitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product(String name, String description, Double price, Double latitude, Double longitude) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public static Product[] populateData() {
        return new Product[] {
                new Product("90s Baggy Low Jeans", "Jeans are a type of pants traditionally made from denim (a kind of cotton fabric). The word most commonly refers to denim blue jeans.", 19.99, 43.6426, -79.3871),
                new Product("Flare Jeans", "Loose-fit, 5-pocket jeans in thick cotton denim. Low waist, zip fly with button, dropped gusset, and straight legs.", 29.99, 43.6426, -79.3875),
                new Product("Skinny High Jeans", "5-pocket jeans in denim with gently stretch. High waist, zip fly with button, and skinny legs.", 9.99, 43.6426, -79.3871),
                new Product("Hill's® Science Diet® Indoor Adult Dry Cat Food", "During your cat's adult years, you want to feed nutrition that supports everyday health and digestion.", 32.89,43.6452, -79.4171),
                new Product("Purina ONE® Tender Selects Adult Cat Dry Food", "Serve your cat a plate of the deliciously crunchy bites and meaty, tender morsels she's been craving with Purina ONE Tender Selects Blend with Real Salmon adult dry cat food.", 42.89, 43.6412, -79.3862),
                new Product("Levis", "Jeans are a type of pants traditionally made from denim (a kind of cotton fabric). The word most commonly refers to denim blue jeans.", 19.99, 43.6426, -79.3871),
                new Product("Dior", "Loose-fit, 5-pocket jeans in thick cotton denim. Low waist, zip fly with button, dropped gusset, and straight legs.", 29.99, 43.6426, -79.3875),
                new Product("Skinny High Jeans", "5-pocket jeans in denim with gently stretch. High waist, zip fly with button, and skinny legs.", 9.99, 43.6426, -79.3871),
                new Product("Freskas Dog food", "During your dog's adult years, you want to feed nutrition that supports everyday health and digestion.", 32.89,43.6452, -79.4171),
                new Product("Purina ONE® Tender Selects Kitten Cat Dry Food", "Serve your cat a plate of the deliciously crunchy bites and meaty, tender morsels she's been craving with Purina ONE Tender Selects Blend with Real Salmon adult dry cat food.", 42.89, 43.6412, -79.3862)
        };
    }
}
