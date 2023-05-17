package com.tongtong.product.service;

import com.tongtong.common.config.AppConfig;
import com.tongtong.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(AppConfig.PRODUCTS_RESOURCE_PATH)
public class ProductService {

    @Autowired
    private ProductServiceImpl productServiceBean;

    public ProductServiceImpl getProductServiceBean() {
        return productServiceBean;
    }

    /**
     * Get all products
     *
     * @return
     */
    @Secured({"Admin", "User"})
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = getProductServiceBean().getProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Get products for a list of ids
     *
     * @return
     */
    @Secured({"Admin", "User"})
    @PostMapping(path = "/ids", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<Product>> getProductsByIds(@RequestBody List<String> ids) {
        List<Product> products = getProductServiceBean().getProductsByIds(ids);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Get product with an id
     *
     * @param id product id to be fetched
     * @return
     */
    @Secured({"Admin", "User"})
    @GetMapping(path = AppConfig.PRODUCT_PATH, produces = "application/json")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) {
        Product product = getProductServiceBean().getProduct(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * Insert a new product. Usage limited to admin role.
     *
     * @param product
     * @return
     * @throws URISyntaxException
     */
    @Secured({"Admin"})
    @PutMapping(path = AppConfig.PRODUCT_PATH, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Product> createProduct(@PathVariable("id") String id,
                                                 @RequestBody Product product) throws URISyntaxException {
        if (!id.equals(product.getId())) {
            return ResponseEntity.badRequest().build();
        }
        boolean success = getProductServiceBean().addProduct(product);
        if (success) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        return new ResponseEntity<>(product, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Update an existing product. Usage limited to admin role.
     *
     * @param id
     * @param product
     * @return
     */
    @Secured({"Admin"})
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Product> modifyProduct(@RequestParam("id") String id, Product product) {
        if (!id.equals(product.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Product modifiedProduct = getProductServiceBean().modifyProduct(product);
        if (modifiedProduct != null) {
            return new ResponseEntity<>(modifiedProduct, HttpStatus.OK);
        }
        return new ResponseEntity<>(product, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Delete a product. Usage limited to admin role.
     *
     * @param id id of product to be deleted.
     * @return
     */
    @Secured({"Admin"})
    @DeleteMapping(path = AppConfig.PRODUCT_PATH,
            consumes = "application/json", produces = "application/text")
    public ResponseEntity<String> removeProduct(@RequestParam("id") String id) {
        boolean removed = getProductServiceBean().removeProduct(id);
        if (removed) {
            return new ResponseEntity<>("Removed product id=" + id, HttpStatus.OK);
        }
        return new ResponseEntity<>("Cannot remove product id=" + id, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Delete all products. Usage limited to admin role.
     *
     * @return
     */
    @Secured({"Admin"})
    @DeleteMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> removeProducts() {
        boolean removed = getProductServiceBean().removeProducts();
        if (removed) {
            return new ResponseEntity<>("ALL PRODUCTS DELETED", HttpStatus.OK);
        }
        return new ResponseEntity<>("PRODUCTS NOT DELETED", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}