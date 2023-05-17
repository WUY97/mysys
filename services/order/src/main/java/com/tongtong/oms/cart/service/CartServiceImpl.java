package com.tongtong.oms.cart.service;

import com.tongtong.oms.cart.dao.CartDao;
import com.tongtong.oms.cart.dao.CartDaoFactory;
import com.tongtong.oms.cart.entity.Cart;
import com.tongtong.oms.cart.entity.CartLine;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides implementation of Cart Service methods
 */
@Component
public class CartServiceImpl {


    @Autowired
    private CartDaoFactory cartDaoFactory;

    private CartDao cartDaoBean;

    @Value("${database.type}")
    private String cartDBType;

    @PostConstruct
    public void postConstruct() {
        this.cartDaoBean = cartDaoFactory.getCartDao("Cart" + cartDBType);
    }

    public void setCartDaoBean(CartDao cartDaoBean) {
        this.cartDaoBean = cartDaoBean;
    }

    public CartDao getCartDaoBean() {
        return cartDaoBean;
    }

    /**
     * @param id unique id of cart
     * @return cart object
     */
    public Cart getCart(String id) {
        Cart cart = getCartDaoBean().getCart(id);
        if (cart == null) {
            cart = new Cart(id);
        } else if (cart.getCartLines() == null) {
            cart.setCartLines(new LinkedList<>());
        }
        return cart;
    }

    /**
     * Writes cart to DB
     *
     * @param cart cart object to be persisted
     * @return
     */
    public boolean saveCart(Cart cart) {
        return getCartDaoBean().saveCart(cart);
    }

    public Cart modifyCart(String id, String productId, float quantity) {
        Cart cart = getCartDaoBean().getCart(id);
        if (cart == null) {
            cart = new Cart(id);
            CartLine cartLine = new CartLine(1, productId, quantity);
            cart.addCartLine(cartLine);
            // Save to db
            saveCart(cart);
            return cart;
        }
        // Cart already exists
        List<CartLine> cartLines = cart.getCartLines();
        if (quantity > 0) {
            // Add or update cartLine
            boolean foundCartLine = false;
            for (CartLine cartLine : cartLines) {
                // Update cart line if product already present
                if (cartLine.getProductId().equals(productId)) {
                    cartLine.setQuantity(cartLine.getQuantity() + quantity);
                    foundCartLine = true;
                    break;
                }
            }
            if (!foundCartLine) {
                // Add new cart line
                int numCartLines = cart.getCartLines().size();
                int seq = (numCartLines == 0) ? 1 : cart.getCartLines().get(numCartLines - 1).getId() + 1;
                CartLine cartLine = new CartLine(seq, productId, quantity);
                cartLines.add(cartLine);
            }
        } else {
            // Remove cart line
            Iterator<CartLine> iter = cartLines.iterator();
            while (iter.hasNext()) {
                CartLine cartLine = iter.next();
                if (cartLine.getProductId().equals(productId)) {
                    iter.remove();
                    break;
                }
            }
        }
        // Save modified cart to DB
        if (!getCartDaoBean().updateCart(cart)) {
            return null;
        }
        return cart;
    }

    /**
     * Remove all carts
     *
     * @return true is successful
     */
    public boolean removeCarts() {
        return getCartDaoBean().removeCarts();
    }

    /**
     * @param id unique id of cart to be removed
     * @return
     */
    public boolean removeCart(String id) {
        return getCartDaoBean().removeCart(id);
    }

}
