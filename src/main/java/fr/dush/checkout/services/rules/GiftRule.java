package fr.dush.checkout.services.rules;

import fr.dush.checkout.domain.Item;
import fr.dush.checkout.domain.Order;
import fr.dush.checkout.domain.OrderItem;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * Parameterizable rule for "for each N (equals) items X, you get K items Y for free"
 */
public class GiftRule implements Rule {

    private final String itemCode;

    private final int boughtNumber;
    private final Item giftItem;
    private final int giftNumber;

    /**
     * Create rule with this parameters
     *
     * @param itemCode     Item which must be bought to get this gift
     * @param boughtNumber Number of this item required to get a gift
     * @param giftItem     Item code of gift
     * @param giftNumber   Number of gift
     */
    public GiftRule(String itemCode, int boughtNumber, Item giftItem, int giftNumber) {
        this.itemCode = itemCode;
        this.boughtNumber = boughtNumber;
        this.giftItem = giftItem;
        this.giftNumber = giftNumber;
    }

    @Override
    public boolean canBeApplied(Collection<String> itemCodes) {
        return itemCodes.contains(itemCode);
    }

    /** Add n gift item for each bundle of item found. */
    @Override
    public void perform(Order order) {
        // Done in 2 times (throw a list) because we can't add an item on the list we are iterating on it.
        List<OrderItem> items = order.filterItems(i -> itemCode.equals(i.getItem().getCode())).collect(toList());

        items.forEach(orderItem -> {
            int bundles = (orderItem.getNumber() - orderItem.getFreeItems()) / boughtNumber;
            if (bundles >= 1) {
                // Add #giftNumber gift item for each bundle. Do not override what was already there
                OrderItem gift = order.addItem(giftItem, bundles * giftNumber);
                gift.setFreeItems(gift.getFreeItems() + bundles * giftNumber);
            }
        });
    }
}
