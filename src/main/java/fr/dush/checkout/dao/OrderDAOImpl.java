package fr.dush.checkout.dao;

import fr.dush.checkout.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderDAOImpl implements OrderDAO {

    private long idGenerator = 1L;

    private Map<Long, Order> orders = new HashMap<>();

    @Override
    public Order save(Order order) {
        // Set auto-generated id
        order.setOrderId(getNextId());

        orders.put(order.getOrderId(), order);

        return order;
    }

    private synchronized long getNextId() {
        return idGenerator++;
    }
}
