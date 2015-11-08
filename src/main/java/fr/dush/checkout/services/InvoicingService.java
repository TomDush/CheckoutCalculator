package fr.dush.checkout.services;

import fr.dush.checkout.domain.Order;

/**
 * This service run order workflow because it's simple (else, another service should be created): save, run rules and
 * compute totals.
 */
public interface InvoicingService {

    /** Finalise order by saving it, finding any gifts or discounts to add and compute total. */
    void finaliseOrder(Order order);
}
