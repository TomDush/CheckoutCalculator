package fr.dush.checkout.services.rules;

import fr.dush.checkout.domain.Order;
import org.springframework.util.Assert;

import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * Parameterizable rule for "buy 3 (equals) items and pay for 2"
 */
public class FreeItemRule implements Rule {

    private final String itemCode;
    private final int expectedNumber;
    private final int freeNumber;

    /**
     * Create rule with this parameters
     *
     * @param itemCode       Item for which this rule apply
     * @param expectedNumber Number of items required for this rule
     * @param freeNumber     Number of free items for each group of #expectedNumber
     */
    public FreeItemRule(String itemCode, int expectedNumber, int freeNumber) {
        Assert.isTrue(isNotEmpty(itemCode), "itemCode should not be empty");

        this.itemCode = itemCode;
        this.expectedNumber = expectedNumber;
        this.freeNumber = freeNumber;
    }

    @Override
    public boolean canBeApplied(Collection<String> itemCodes) {
        return itemCodes.contains(itemCode);
    }

    /**
     * Make n items free for each bundle. This rule can be applied several times and already free items doesn't count
     * (but are not overrided).
     */
    @Override
    public void perform(Order order) {
        order.filterItems(i -> itemCode.equals(i.getItem().getCode())).forEach(orderItem -> {
            int bundles = (orderItem.getNumber() - orderItem.getFreeItems()) / expectedNumber;
            orderItem.setFreeItems(orderItem.getFreeItems() + freeNumber * bundles);
        });
    }
}
