package org.example.warehouse;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {

    private final String name;
    private static Warehouse instance;

    private Set<ProductRecord> changedProducts = new HashSet<>();



    private Warehouse(String name) {
        this.name = name;
    }

    private List<ProductRecord> products = new ArrayList<>();




    public static Warehouse getInstance(String name) {
        if (instance == null) {
            instance = new Warehouse(name);
        } else instance.clearProducts();
        return instance;
    }
    public static Warehouse getInstance() {
        return getInstance("MyStore");
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public List<ProductRecord> getProducts() {
        return Collections.unmodifiableList(products); // return truly immutable unable to be modified
    }

    public ProductRecord addProduct(UUID id, String name, Category category, BigDecimal price) {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }

        if (id != null && getProductById(id).isPresent()) {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }


        price = (price != null) ? price : BigDecimal.ZERO;
        ProductRecord product = new ProductRecord(id != null ? id : UUID.randomUUID(), name, category, price);
        products.add(product);
        return product;

    }

    public void clearProducts() {
        products.clear();
        changedProducts.clear();
    }

    public Optional<ProductRecord> getProductById(UUID id) {
        return products.stream()
                .filter(product -> product.uuid().equals(id))
                .findFirst();
    }

    public List<ProductRecord> getProductsBy(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        return products.stream()
                .filter(product -> product.category().equals(category))
                .toList();
    }

    // update price from extended immutable record

    public ProductRecord updateProductPrice(UUID id, BigDecimal newPrice) {
        Optional<ProductRecord> productOpt = getProductById(id);


        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product with that id doesn't exist.");
        }

        ProductRecord product = productOpt.get();

        changedProducts.add(product);

        ProductRecord updatedProduct = new ProductRecord(product.uuid(), product.name(), product.category(), newPrice);

        // Replace the existing product with the updated one in the products list.
        products.remove(product);
        products.add(updatedProduct);

        return updatedProduct;
    }

    public List<ProductRecord> getChangedProducts() {
        return List.copyOf(changedProducts);
    }


    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {

        return products.stream().collect(Collectors.groupingBy(ProductRecord::category));
    }
}
