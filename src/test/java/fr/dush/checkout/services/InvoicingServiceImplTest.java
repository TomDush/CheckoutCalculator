package fr.dush.checkout.services;

import fr.dush.checkout.dao.OrderDAO;
import fr.dush.checkout.domain.Item;
import fr.dush.checkout.domain.Order;
import fr.dush.checkout.services.rules.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static fr.dush.checkout.tools.PriceTools.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvoicingServiceImplTest {

    @InjectMocks
    private InvoicingServiceImpl invoicingService;

    @Mock
    private RuleRepository ruleRepository;
    @Mock
    private OrderDAO orderDAO;

    @Mock
    private Rule rule1;
    @Mock
    private Rule rule2;

    /** (Very) Simple case where no rules has been selected. Service will only compute total. */
    @Test
    public void testPerformRules_noRules() throws Exception {
        // No rules are going to be returned in this case!!
        reset(ruleRepository);

        // Generate test data
        Order order = new Order();
        order.addItem(new Item("PS4-CONSOLE", "Ps4", newBigDecimal(199.99)), 1);
        order.addItem(new Item("PS4-MGSV", "MGS for PS4", newBigDecimal(49.99)), 1);
        order.addItem(new Item("PS4-STICKER", "Any sticker to customise a PS4", newBigDecimal(2.99)), 2);

        // Exec tested method
        invoicingService.finaliseOrder(order);

        // Assert total is correct
        assertThat(order.getTotal()).isEqualTo(newBigDecimal(255.96));
    }

    /** Test with real discount rules */
    @Test
    public void testPerformRules_withRules() throws Exception {
        // Rules are actually doing nothing. Report is generated with discount and gift
        when(ruleRepository.findRulesFor(anyListOf(String.class))).thenReturn(Arrays.asList(rule1, rule2));

        // Generate test data
        Order order = new Order();
        order.addItem(new Item("PS4-CONSOLE", "Ps4", newBigDecimal(199.99)), 1);
        order.addItem(new Item("PS4-MGSV", "MGS for PS4", newBigDecimal(49.99)), 1).setDiscount(newBigDecimal(20));
        order.addItem(new Item("PS4-STICKER", "Any sticker to customise a PS4", newBigDecimal(2.99)), 2)
             .setDiscount(newBigDecimal(2));
        order.addItem(new Item("PS4-REMOTE", "PS4 Remote", newBigDecimal(69.99)), 4).setFreeItems(1);

        // Exec tested method
        invoicingService.finaliseOrder(order);

        // Assert total is correct - with some gifts and discount this time!
        assertThat(order.getTotal()).isEqualTo(newBigDecimal(443.93));

        // Assert Sub-totals are correct
        order.filterItems(i -> "PS4-REMOTE".equals(i.getItem().getCode()))
             .forEach(i -> assertThat(i.getSubTotal()).isEqualTo(newBigDecimal(209.97)));
        order.filterItems(i -> "PS4-STICKER".equals(i.getItem().getCode()))
             .forEach(i -> assertThat(i.getSubTotal()).isEqualTo(newBigDecimal(3.98)));

        // Verify all rules have been called
        verify(rule1).perform(order);
        verify(rule2).perform(order);
        verify(ruleRepository).findRulesFor(newHashSet());
    }

    /** Could be found in Guava lib */
    private static Set<String> newHashSet() {
        return new HashSet<>(Arrays.asList("PS4-CONSOLE", "PS4-MGSV", "PS4-STICKER", "PS4-REMOTE"));
    }

}