package fr.dush.checkout.services.rules;

import fr.dush.checkout.domain.Order;

import java.math.BigDecimal;
import java.util.Collection;

import static fr.dush.checkout.tools.PriceTools.*;

/**
 * Parameterizable rule for "buy 2 (equals) items for a special price"
 */
public class SpecialPriceRule implements Rule {

    private final String itemCode;

    private final int number;
    private final BigDecimal specialPrice;

    /**
     * Create rule with this parameters
     *
     * @param itemCode     Item for which this rule apply
     * @param number       Number of items required for this rule
     * @param specialPrice Price for the full bundle (to use instead of the normal price)
     */
    public SpecialPriceRule(String itemCode, int number, BigDecimal specialPrice) {
        this.itemCode = itemCode;
        this.number = number;
        this.specialPrice = specialPrice;
    }

    @Override
    public boolean canBeApplied(Collection<String> itemCodes) {
        return itemCodes.contains(itemCode);
    }

    /** For each bundle, we apply a discount to get the final expected price of this bundle */
    @Override
    public void perform(Order order) {
        order.filterItems(orderItem -> itemCode.equals(orderItem.getItem().getCode())).forEach(orderItem -> {
            // Number of full bundle - excluding already free items
            int bundles = (orderItem.getNumber() - orderItem.getFreeItems()) / number;
            // Compute discount to apply for each bundle
            BigDecimal discount = orderItem.getItem().getPrice().multiply(newBigDecimal(number)).subtract(specialPrice);
            // Add this discount to the one already present
            orderItem.setDiscount(orderItem.getDiscount().add(discount.multiply(newBigDecimal(bundles))));
        });
    }
}
