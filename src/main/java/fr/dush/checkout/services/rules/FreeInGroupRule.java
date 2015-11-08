package fr.dush.checkout.services.rules;

import fr.dush.checkout.domain.Order;
import fr.dush.checkout.domain.OrderItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * Parameterizable rule for "buy 3 (in a set of items) and the cheapest is free"
 */
public class FreeInGroupRule implements Rule {

    private final List<String> itemCodes;
    private final int expectedNumber;

    /**
     * Create rule with this parameters
     *
     * @param itemCodes      Items for which this rule apply
     * @param expectedNumber Number of items required for this rule
     */
    public FreeInGroupRule(List<String> itemCodes, int expectedNumber) {
        this.itemCodes = itemCodes;
        this.expectedNumber = expectedNumber;
    }

    @Override
    public boolean canBeApplied(Collection<String> codes) {
        // This rule can be applied if at least 1 of item has been found
        return this.itemCodes.stream().filter(codes::contains).findFirst().isPresent();
    }

    /** Find the most intesting bundle grouping for user and set 1 free for each of this bundle */
    @Override
    public void perform(Order order) {
        // Extract orderItems concerned by this rule
        List<OrderItem> orderItems = order.filterItems(i -> itemCodes.contains(i.getItem().getCode())).collect(toList());

        // Count number of elements, then number of present bundles
        Integer count = orderItems.stream()
                                  .map(orderItem -> orderItem.getNumber() - orderItem.getFreeItems())
                                  .reduce(0, (a, b) -> a + b);
        int bundles = count / expectedNumber;

        // No need to go further if no bundle found
        if(bundles > 0) {
            // Sort bundles to set 1 free per bundle (ASC price)
            Collections.sort(orderItems, (i1, i2) -> i2.getItem().getPrice().compareTo(i1.getItem().getPrice()));

            // In this list, we really have 1 OrderItem per ordered item
            List<OrderItem> items = new ArrayList<>();
            orderItems.forEach(orderItem -> {
                for (int i = 0; i < orderItem.getNumber() - orderItem.getFreeItems(); i++) {
                    items.add(orderItem);
                }
            });

            // Then it's easy to select items to set free
            for (int i = 1; i <= bundles; i++) {
                OrderItem orderItem = items.get(i * expectedNumber - 1);
                orderItem.setFreeItems(orderItem.getFreeItems() + 1);
            }
        }
    }
}
