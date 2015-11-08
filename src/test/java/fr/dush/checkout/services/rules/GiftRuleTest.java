package fr.dush.checkout.services.rules;

import fr.dush.checkout.domain.Item;
import fr.dush.checkout.domain.Order;
import org.junit.Before;
import org.junit.Test;

import static fr.dush.checkout.tools.PriceTools.*;
import static org.assertj.core.api.Assertions.*;

public class GiftRuleTest {

    private GiftRule rule;

    @Before
    public void setUp() throws Exception {
        // Create rule
        rule = new GiftRule("PS4-CONSOLE", 2, new Item("PS4-REMOTE", "PS4 Remote", newBigDecimal(69.99)), 4);

    }

    /** Happy path with nothing to do */
    @Test
    public void testPerform_empty() throws Exception {

        // Order without any item matching
        Order order = new Order();
        order.addItem(new Item("PS4-CONSOLE", "Ps4", newBigDecimal(199.99)), 1);

        // Exec
        rule.perform(order);

        // Assert - nothing added
        assertThat(order.getItems()).hasSize(1);
    }

    /** Simple path: rule applied only once, no other remotes was already there */
    @Test
    public void testPerform_simple() throws Exception {
        // Order without any item matching
        Order order = new Order();
        order.addItem(new Item("PS4-CONSOLE", "Ps4", newBigDecimal(199.99)), 2);

        // Exec
        rule.perform(order);

        // Assert - 4 free remotes added
        assertThat(order.getItems()).hasSize(2);
        order.getItems().forEach(i -> {
            if ("PS4-REMOTE".equals(i.getItem().getCode())) {
                // 4 remotes free when buying 2 console -> 4
                assertThat(i.getNumber()).isEqualTo(4);
                assertThat(i.getFreeItems()).isEqualTo(4);
            }
        });
    }

    /** Complex case when a remotes was already there, including a free one */
    @Test
    public void testPerform_complex() throws Exception {
        // Order without any item matching
        Order order = new Order();
        order.addItem(new Item("PS4-CONSOLE", "Ps4", newBigDecimal(199.99)), 6).setFreeItems(1);
        order.addItem(new Item("PS4-REMOTE", "PS4 Remote", newBigDecimal(69.99)), 2).setFreeItems(1);

        // Exec
        rule.perform(order);

        // Assert - 8 free remotes added
        assertThat(order.getItems()).hasSize(2);
        order.getItems().forEach(i -> {
            if ("PS4-REMOTE".equals(i.getItem().getCode())) {
                // already 2 remotes including 1 free
                // 2 full bundle (1 console was free) -> +8 items and +8 free
                assertThat(i.getNumber()).isEqualTo(10);
                assertThat(i.getFreeItems()).isEqualTo(9);
            }
        });
    }
}