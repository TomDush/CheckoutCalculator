package fr.dush.checkout;

import fr.dush.checkout.dao.ItemDAO;
import fr.dush.checkout.domain.Item;
import fr.dush.checkout.domain.Order;
import fr.dush.checkout.services.InvoicingService;
import fr.dush.checkout.services.ReceiptWriterImpl;
import fr.dush.checkout.services.RuleRepository;
import fr.dush.checkout.services.rules.FreeInGroupRule;
import fr.dush.checkout.services.rules.FreeItemRule;
import fr.dush.checkout.services.rules.GiftRule;
import fr.dush.checkout.services.rules.SpecialPriceRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import java.util.Arrays;

import static fr.dush.checkout.tools.PriceTools.*;
import static org.assertj.core.api.Assertions.*;

/**
 * This is main test class used to show how to use this app (which has no UI) but also test Spring components
 * integration.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CheckoutCalculator.class)
public class CheckoutCalculatorTest {

    @Autowired
    private ItemDAO itemDAO;
    @Autowired
    private InvoicingService invoicingService;
    @Autowired
    private ReceiptWriterImpl receiptWriter;

    /**
     * The point of this test is <b>not</b> to test every case! Special case are tested on unit-test. Here the goal is
     * to check spring components integration and a use case of this (partial) app.
     */
    @Test
    public void testMyCheckoutCalculator() throws Exception {
        // Customer want to buy a PS4 with 4 remotes in total, 3 games for him, 1 for a friend and 3 stickers

        // For him
        Order order = new Order();
        order.addItem(itemDAO.findByCode("PS4-CONSOLE").get(), 1);
        order.addItem(itemDAO.findByCode("PS4-REMOTE").get(), 3);

        order.addItem(itemDAO.findByCode("PS4-MGSV").get(), 1);
        order.addItem(itemDAO.findByCode("PS4-WITCHER").get(), 1);
        order.addItem(itemDAO.findByCode("PS4-GTAV").get(), 1);

        order.addItem(itemDAO.findByCode("PS4-STICKER").get(), 3);

        // For his friend - save order! (yes, he buy twice the same game)
        order.addItem(itemDAO.findByCode("PS4-WITCHER").get(), 1);

        // And now, he validate his order
        invoicingService.finaliseOrder(order);

        // He will receive this receipt, print on system out.
        // We don't test this output here.
        receiptWriter.write(order, System.out);

        // But we test if it's the expected final amount
        // If it is, rules has been properly done, subtotals should be good and evrything works. But again, proper
        // tests are done in unit-tests
        assertThat(order.getTotal().doubleValue()).isEqualTo(565.94);

        // Yes, rule on games will take the most interesting bundle for the customer: here the second PS4-WITCHER,
        // and not the PS4-MGSV which is cheaper. Virtual bundle is: 1x PS4-GTAV + 2x PS4-WITCHER

    }

    /**
     * It initiate database in unit test context. Because it's always nicer to test with concrete data, I used console
     * context for this tests.
     */
    @Component
    public static class DatabaseInitialiser {

        @Autowired
        private ItemDAO itemDAO;
        @Autowired
        private RuleRepository ruleRepository;

        /** Initialise database */
        @PostConstruct
        public void initializeDatabase() {
            // Items stored in database, management of this list are not include in this project.
            itemDAO.save(new Item("PS4-CONSOLE", "Ps4", newBigDecimal(199.99)));

            Item remote = itemDAO.save(new Item("PS4-REMOTE", "PS4 Remote", newBigDecimal(69.99)));

            itemDAO.save(new Item("PS4-MGSV", "Game: MGS V", newBigDecimal(49.99)));
            itemDAO.save(new Item("PS4-WITCHER", "Game: The Witcher", newBigDecimal(59.99)));
            itemDAO.save(new Item("PS4-GTAV", "Game: GTA V", newBigDecimal(69.99)));
            itemDAO.save(new Item("PS4-POKEMON", "Game: POKEMON", newBigDecimal(39.99)));

            itemDAO.save(new Item("PS4-STICKER", "PS4 sticker", newBigDecimal(2.99)));

            // In this project version, we assume Rules are already loaded into RuleRetriever. In real-word project,
            // this "loader" should be implemented to work live...

            // 'for each N (equals) items X, you get K items Y for free' -> 1 console bought = 1 free remote
            ruleRepository.add(new GiftRule("PS4-CONSOLE", 1, remote, 1));
            // 'buy 2 (equals) items for a special price' -> 3 console bought for 180 instead of 209.97
            ruleRepository.add(new SpecialPriceRule("PS4-REMOTE", 3, newBigDecimal(180)));
            // 'buy 3 (in a set of items) and the cheapest is free' -> 3 game bought = 1 free (the cheapest)
            ruleRepository.add(new FreeInGroupRule(Arrays.asList("PS4-MGSV", "PS4-WITCHER", "PS4-GTAV", "PS4-POKEMON"),
                                                   3));
            // 'buy 3 (equals) items and pay for 2' -> 3 stickers bought = pay only 2, 1 is free
            ruleRepository.add(new FreeItemRule("PS4-STICKER", 3, 1));

        }

    }
}