package devenv.ips.service;

import devenv.ips.entity.product.Product;
import devenv.ips.repository.ProductRepository;
import devenv.ips.entity.user.User;
import devenv.ips.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import devenv.ips.repository.OrderRepository;
import devenv.ips.entity.orders.Order;
import devenv.ips.entity.orders.OrderStatus;
@Service
public class PaymentService {
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final UserRepository userRepo;

    public PaymentService(ProductRepository pr, OrderRepository or, UserRepository ur) {
        this.productRepo = pr;
        this.orderRepo = or;
        this.userRepo = ur;
    }

    @Transactional
    public Order processPayment(Long productId) {
        // 1. Get current logged-in user email from JWT context
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findByEmail(email).orElseThrow();

        // 2. Find Product
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 3. Simple stock check
        if (product.getStockQuantity() <= 0) throw new RuntimeException("Out of stock");

        // 4. Simulate Payment Logic
        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setAmountPaid(product.getPrice());
        order.setStatus(OrderStatus.COMPLETED);

        // 5. Update Stock
        product.setStockQuantity(product.getStockQuantity() - 1);
        productRepo.save(product);

        return orderRepo.save(order);
    }
}