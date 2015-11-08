package fr.dush.checkout.services.rules;

import fr.dush.checkout.domain.Item;
import fr.dush.checkout.domain.Order;
import org.junit.Test;

import static fr.dush.checkout.tools.PriceTools.*;
import static org.assertj.core.api.Assertions.*;

public class FreeItemRuleTest {

    /** Happy path with nothing to do */
    @Test
    public void testPerform_empty() throws Exception {
        // Create rule
        FreeItemRule rule = new FreeItemRule("PS4-REMOTE", 2, 2);

        // Order without any item matching
        Order order = new Order();
        order.addItem(new Item("PS4-CONSOLE", "Ps4", newBigDecimal(199.99)), 2);
        order.addItem(new Item("PS4-REMOTE", "PS4 Remote", newBigDecimal(69.99)), 1);

        // Exec
        rule.perform(order);

        // Assert
        order.getItems().forEach(i -> assertThat(i.getFreeItems()).isEqualTo(0));
    }

    /** Complex case when a free remote has already been added (by another rule) and there are several free items */
    @Test
    public void testPerform_complex() throws Exception {
        // Create rule
        FreeItemRule rule = new FreeItemRule("PS4-REMOTE", 2, 2);

        // Generate test data
        Order order = new Order();
        order.addItem(new Item("PS4-CONSOLE", "Ps4", newBigDecimal(199.99)), 2);
        order.addItem(new Item("PS4-REMOTE", "PS4 Remote", newBigDecimal(69.99)), 6).setFreeItems(1);

        // Exec
        rule.perform(order);

        // Assert
        order.getItems().forEach(i -> {
            if ("PS4-REMOTE".equals(i.getItem().getCode())) {
                // One was already free -> 1
                // We can apply the rule only twice because 1 item is already free -> +4
                assertThat(i.getFreeItems()).isEqualTo(5);
            } else {
                // Doesn't apply
                assertThat(i.getFreeItems()).isEqualTo(0);
            }
        });
    }
}