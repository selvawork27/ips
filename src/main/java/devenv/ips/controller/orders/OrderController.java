package devenv.ips.controller.orders;

import devenv.ips.entity.product.Product;
import devenv.ips.repository.ProductRepository;
import devenv.ips.repository.OrderRepository;

import devenv.ips.repository.UserRepository;
import devenv.ips.entity.orders.Order;
import devenv.ips.entity.user.User;
import devenv.ips.entity.orders.OrderStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderController(OrderRepository orderRepo, ProductRepository productRepo, UserRepository userRepo) {
        this.orderRepository = orderRepo;
        this.productRepository = productRepo;
        this.userRepository = userRepo;
    }
    @PostMapping("/checkout/{productId}")
    public ResponseEntity<?> createOrder(@PathVariable Long productId) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getStockQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Product is out of stock");
        }
        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setStatus(OrderStatus.PENDING);
        order.setAmountPaid(product.getPrice());

        product.setStockQuantity(product.getStockQuantity() - 1);
        productRepository.save(product);

        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }
}