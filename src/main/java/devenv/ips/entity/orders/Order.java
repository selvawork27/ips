package devenv.ips.entity.orders;

import devenv.ips.common.BaseEntity;
import devenv.ips.entity.product.Product;
import devenv.ips.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order extends BaseEntity {
    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    private BigDecimal amountPaid;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // PENDING, COMPLETED, FAILED
}

