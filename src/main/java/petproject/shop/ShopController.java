package petproject.shop;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080")
public class ShopController {
    
    private List<Product> products = Arrays.asList(
        new Product(1L, "Товар 1", 100.0),
        new Product(2L, "Товар 2", 200.0),
        new Product(3L, "Товар 3", 300.0),
        new Product(4L, "Товар 4", 400.0),
        new Product(5L, "Товар 5", 500.0),
        new Product(6L, "Товар 6", 600.0)
    );

    @GetMapping("/products")
    public List<Product> getProducts() {
        return products;
    }

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, String>> checkout(@RequestBody List<CartItem> cartItems) {
        try {
            double total = calculateTotal(cartItems);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Заказ оформлен успешно! Общая сумма: " + total + " руб.");
            response.put("orderId", generateOrderId());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Ошибка при оформлении заказа: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    private double calculateTotal(List<CartItem> cartItems) {
        return cartItems.stream()
            .mapToDouble(item -> {
                Product product = products.stream()
                    .filter(p -> p.getId().equals(item.getProductId()))
                    .findFirst().orElse(null);
                return product != null ? product.getPrice() * item.getQuantity() : 0;
            }).sum();
    }
    
    private String generateOrderId() {
        return "ORD-" + System.currentTimeMillis();
    }
    
    public static class CartItem {
        private Long productId;
        private int quantity;
        
        public CartItem() {}
        
        public CartItem(Long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
        
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}