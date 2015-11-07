package fr.dush.checkout.dao;

import fr.dush.checkout.domain.Order;

/**
 * Data access to orders. Some methods are missing for a real-word project.
 */
public interface OrderDAO {

    /** Save order and generate it's database id */
    Order save(Order order);
}
