package com.example.product.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.product.R;
import com.example.product.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private final List<Product> products;
    private List<Product> filteredProducts = new ArrayList<>();

    public ProductAdapter(List<Product> products) {
        this.products = products;
        if (products != null) {
            filteredProducts.addAll(products);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_row, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.name.setText(filteredProducts.get(position).getName());
        holder.description.setText(filteredProducts.get(position).getDescription());
        holder.price.setText(filteredProducts.get(position).getPrice().toString());

        holder.productRowCardView.setOnClickListener(contactView -> {
            Intent productDetailIntent = new Intent(context, ProductEntryActivity.class);
            productDetailIntent.putExtra("product", filteredProducts.get(position));
            productDetailIntent.putExtra("saveBtn", false);
            context.startActivity(productDetailIntent);

        });
    }

    @Override
    public int getItemCount() {
        return filteredProducts.size();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {

        private final CardView productRowCardView;

        private final TextView name;
        private final TextView description;
        private final TextView price;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            productRowCardView = itemView.findViewById(R.id.productRowCardView);
        }
    }

    // Tell the IDE don't show this warning message
    @SuppressWarnings("notifyDataSetChanged")
    public void filter(String newText) {
        newText = newText.toLowerCase();

        if (newText.length() == 0) {
            filteredProducts.clear();
            filteredProducts.addAll(products);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(newText);
            filteredProducts = products.stream().filter(product -> product.getName().toLowerCase().contains(sb) || product.getDescription().toLowerCase().contains(sb))
                    .collect(Collectors.toList());

        }
        notifyDataSetChanged();
    }
}
