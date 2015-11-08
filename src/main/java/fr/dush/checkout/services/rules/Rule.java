package fr.dush.checkout.services.rules;

import fr.dush.checkout.domain.Order;

import java.util.Collection;
import java.util.List;

/** Any rule should implement this interface */
public interface Rule {

    /** Check if the rule can be applied to at least or of this item.
     * @param itemCodes*/
    boolean canBeApplied(Collection<String> itemCodes);

    /** Perform this rule on this order. Rule is going to change the order to add discounts or gifts. */
    void perform(Order order);
}
