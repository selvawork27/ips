package devenv.ips.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import devenv.ips.entity.orders.Order;
import devenv.ips.entity.orders.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserEmail(String email);
    List<Order> findByStatus(OrderStatus status);
}