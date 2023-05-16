package com.tongtong.oms.cart.dao.sql;

import com.tongtong.oms.cart.entity.Cart;
import com.tongtong.oms.cart.entity.CartLine;

import java.util.LinkedList;
import java.util.List;

public class DBCartLine {
    private String cartId;
    private int cartLineId;

    private String productId;
    private float quantity;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public int getCartLineId() {
        return cartLineId;
    }

    public void setCartLineId(int cartLineId) {
        this.cartLineId = cartLineId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + cartLineId + "\"" + ", " +
                "\"cartId\":" + (cartId == null ? "null" : "\"" + cartId + "\"") + ", " +
                "\"productId\":" + (productId == null ? "null" : "\"" + productId + "\"") + ", " +
                "\"quantity\":\"" + quantity + "\"" +
                "}";
    }

    public static List<DBCartLine> createDBCartLines(Cart cart) {
        List<DBCartLine> dbCartLines = new LinkedList<>();
        for (CartLine cartLine : cart.getCartLines()) {
            DBCartLine dbCartLine = new DBCartLine();
            dbCartLine.setCartLineId(cartLine.getId());
            dbCartLine.setCartId(cart.getId());
            dbCartLine.setProductId(cartLine.getProductId());
            dbCartLine.setQuantity(cartLine.getQuantity());
            dbCartLines.add(dbCartLine);
        }
        return dbCartLines;
    }

    public static Cart getCart(String cartId, List<DBCartLine> dbCartLines) {
        Cart cart = new Cart();
        cart.setId(cartId);
        for (DBCartLine dbCartLine : dbCartLines) {
            CartLine cartLine = new CartLine();
            cartLine.setId(dbCartLine.getCartLineId());
            cartLine.setProductId(dbCartLine.getProductId());
            cartLine.setQuantity(dbCartLine.getQuantity());
            cart.getCartLines().add(cartLine);
        }
        return cart;
    }
}
