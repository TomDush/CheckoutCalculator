package fr.dush.checkout.services;

import fr.dush.checkout.domain.Order;

/**
 * @author Thomas Duchatelle
 */
public interface InvoicingService {

    /** Finalise order by saving it, finding any gifts or discounts to add and compute total. */
    void finaliseOrder(Order order);
}
