package fr.dush.checkout.services.rules;

import fr.dush.checkout.domain.Item;
import fr.dush.checkout.domain.Order;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static fr.dush.checkout.tools.PriceTools.*;
import static org.assertj.core.api.Assertions.*;

public class FreeInGroupRuleTest {

    private FreeInGroupRule rule;

    @Before
    public void setUp() throws Exception {
        // Create rule - reduce by 2 every 4 items
        rule = new FreeInGroupRule(Arrays.asList("PS4-MGSV", "PS4-GTA", "PS4-WITCHER"), 3);
    }

    /** Happy path with nothing to do */
    @Test
    public void testPerform_empty() throws Exception {

        // Order without any item matching
        Order order = new Order();
        order.addItem(new Item("PS4-MGSV", "Game 1", newBigDecimal(42.99)), 1);
        order.addItem(new Item("PS4-GTA", "Game 2", newBigDecimal(52.99)), 1);
        order.addItem(new Item("PS4-POKEMON", "Game 3", newBigDecimal(32.99)), 1);

        // Exec
        rule.perform(order);

        // Assert - no free items
        order.getItems().forEach(i -> assertThat(i.getFreeItems()).isEqualTo(0));
    }

    /** Complex case when a free item has already been added and there are several free items */
    @Test
    public void testPerform_complex() throws Exception {

        // Order without any item matching
        Order order = new Order();
        order.addItem(new Item("PS4-MGSV", "Game 1", newBigDecimal(42.99)), 3).setFreeItems(1);
        order.addItem(new Item("PS4-GTA", "Game 2", newBigDecimal(52.99)), 6).setFreeItems(2);
        order.addItem(new Item("PS4-POKEMON", "Game 3", newBigDecimal(32.99)), 1);

        // Exec
        rule.perform(order);

        // Assert - no free items
        order.getItems().forEach(orderItem -> {
            // Most interesting bundle (for user) are:
            // - 3 x PS4-GTA => +1 free
            // - 1 x PS4-GTA + 2 x PS4-MGSV => +1 PS4-MGSV free
            if ("PS4-MGSV".equals(orderItem.getItem().getCode())) {
                assertThat(orderItem.getFreeItems()).isEqualTo(2);
            } else if ("PS4-GTA".equals(orderItem.getItem().getCode())) {
                assertThat(orderItem.getFreeItems()).isEqualTo(3);
            } else {
                assertThat(orderItem.getFreeItems()).isEqualTo(0);
            }
        });
    }
}