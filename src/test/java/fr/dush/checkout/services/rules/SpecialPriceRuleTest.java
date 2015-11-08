package fr.dush.checkout.services.rules;

import fr.dush.checkout.domain.Item;
import fr.dush.checkout.domain.Order;
import fr.dush.checkout.domain.OrderItem;
import org.assertj.core.data.Offset;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static fr.dush.checkout.tools.PriceTools.*;
import static org.assertj.core.api.Assertions.*;

public class SpecialPriceRuleTest {

    private SpecialPriceRule rule;

    @Before
    public void setUp() throws Exception {
        // Create rule - reduce by 2 every 4 items
        rule = new SpecialPriceRule("PS4-STICKER", 4, newBigDecimal(10));

    }

    /** Happy path with nothing to do */
    @Test
    public void testPerform_empty() throws Exception {

        // Order without any item matching
        Order order = new Order();
        order.addItem(new Item("PS4-STICKER", "Sticker", newBigDecimal(2.99)), 3);

        // Exec
        rule.perform(order);

        // Assert - no discount
        order.getItems()
             .forEach(i -> assertThat(round(i.getDiscount())).isCloseTo(BigDecimal.ZERO,
                                                                        Offset.offset(newBigDecimal(0.1))));
    }

    /** Complex case when a free item has already been added and there are several free items */
    @Test
    public void testPerform_complex() throws Exception {
        // Order without any item matching
        Order order = new Order();
        OrderItem item = order.addItem(new Item("PS4-STICKER", "Sticker", newBigDecimal(2.99)), 12);
        item.setFreeItems(1);
        item.setDiscount(newBigDecimal(1.5));

        // Exec
        rule.perform(order);

        // Assert - 2 full bundle -> final price is 2 * 10 + 3 * 2.99 - 1.5 = 27.47, instead of 11 * 2.99 = 32.89
        order.getItems().forEach(i -> assertThat(round(i.getDiscount())).isEqualTo(newBigDecimal(5.42)));
    }

}