package com.tongtong.oms.cart.entity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Cart {
    private String id;
    private List<CartLine> cartLines;

    public Cart() {
        this.cartLines = new LinkedList<>();
    }

    public Cart(String id) {
        this.id = id;
        this.cartLines = new LinkedList<>();
    }

    public Cart(Cart cartEntity) {
        this.id = cartEntity.id;
        this.cartLines = cartEntity.cartLines;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CartLine> getCartLines() {
        return cartLines;
    }

    public void setCartLines(List<CartLine> cartLines) {
        this.cartLines = cartLines;
    }

    public void addCartLine(CartLine cartLine) {
        this.getCartLines().add(cartLine);
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
                "\"cartLines\":" + (cartLines == null ? "null" : Arrays.toString(cartLines.toArray())) +
                "}";
    }
}
