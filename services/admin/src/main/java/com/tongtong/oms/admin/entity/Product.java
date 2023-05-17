package com.tongtong.oms.admin.entity;

public class Product {
    private String id;
    private String name;
    private Float price;
    private String imageUri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
                "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
                "\"price\":" + (price == null ? "null" : "\"" + price + "\"") + ", " +
                "\"imageUrl\":" + (imageUri == null ? "null" : "\"" + imageUri + "\"") +
                "}";
    }
}
