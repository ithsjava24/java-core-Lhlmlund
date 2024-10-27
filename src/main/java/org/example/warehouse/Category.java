package org.example.warehouse;

import java.util.HashMap;
import java.util.Map;

public class Category {

    private String name;
    private static final Map<String, Category> categories = new HashMap<>();

    private Category(String name) {
        this.name = capitalizeName(name);
    }


    public static Category of(String name) {

        if (name == null) {

            throw new IllegalArgumentException("Category name can't be null");

        }

        String capitalized = capitalizeName(name);
        return categories.computeIfAbsent(capitalized, Category::new);
    }


    private static String capitalizeName(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public String getName() {

        return name;
    }
}
