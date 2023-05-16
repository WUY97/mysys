package com.tongtong.oms.cart.service;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.config.ServiceID;
import com.tongtong.common.status.ServiceAgent;
import com.tongtong.oms.cart.entity.Cart;
import com.tongtong.common.entity.Role;
import com.tongtong.common.security.Secured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * Rest interface for adding, searching, modifying and deleting products
 */
@RestController
@RequestMapping(AppConfig.CARTS_RESOURCE_PATH)
public class CartService {

    @Autowired
    private CartServiceImpl cartServiceBean;

    public CartServiceImpl getCartServiceBean() {
        return cartServiceBean;
    }


    @Secured({Role.ADMIN, Role.USER})
    @GetMapping(path= AppConfig.CART_PATH, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> getCart(@PathVariable("id") String id) {
        if (id.equals("")) {
            id = getUser();
        } else if (!id.equals(getUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Cart cart = getCartServiceBean().getCart(id);
        return ResponseEntity.ok().body(cart);
    }

    @Secured({Role.ADMIN, Role.USER})
    @PutMapping(path= AppConfig.CART_PATH, consumes=MediaType.APPLICATION_JSON_VALUE,
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> saveCart(@PathVariable("id") String id, @RequestBody Cart cart) {
        if (!cart.getId().equals(getUser()) || !cart.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        boolean success = getCartServiceBean().saveCart(cart);
        if (success) {
            try {
                return ResponseEntity.created(new URI("/cart")).body(cart);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cart);
    }

    @Secured({Role.ADMIN, Role.USER})
    @PostMapping(consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity modifyCart(@RequestParam("id") String id,
                            @RequestParam("productId") String productId,
                            @RequestParam("quantity") float quantity) {
        if (id == null || id.equals("")) {
            id = getUser();
        } else if (!id.equals(getUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Cart modifiedCart = getCartServiceBean().modifyCart(id, productId, quantity);
        if (modifiedCart != null) {
            try {
                return ResponseEntity.created(new URI("/cart")).body("Saved cart");
            } catch (URISyntaxException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Secured({Role.ADMIN, Role.USER})
    @DeleteMapping(path= AppConfig.CART_PATH, consumes=MediaType.APPLICATION_JSON_VALUE,
            produces=MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> removeCart(@PathVariable("id") String id) {
        if (id.equals(getUser())) {
            if (getCartServiceBean().removeCart(id)) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Secured({Role.ADMIN})
    @DeleteMapping(produces=MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> removeCarts() {
        if (getCartServiceBean().removeCarts()) {
                return ResponseEntity.ok().body("REMOVED ALL CARTS");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private String getUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
