package fr.dush.checkout.services;

import fr.dush.checkout.domain.Item;
import fr.dush.checkout.domain.Order;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static fr.dush.checkout.tools.PriceTools.*;
import static org.assertj.core.api.Assertions.*;

public class ReceiptWriterImplTest {

    private ReceiptWriterImpl receiptWriter = new ReceiptWriterImpl();

    @Test
    public void testWriteComplexReceipt() throws Exception {
        // Generate test data
        Order order = new Order();
        order.setOrderId(1542L);
        order.addItem(new Item("PS4-CONSOLE", "Ps4", newBigDecimal(199.99)), 1).setSubTotal(newBigDecimal(179.99));
        order.addItem(new Item("PS4-REMOTE", "PS4 Remote", newBigDecimal(69.99)), 4).setSubTotal(newBigDecimal(159.88));
        order.addItem(new Item("PS4-MGSV", "MGS for PS4", newBigDecimal(49.99)), 1).setSubTotal(newBigDecimal(49.99));
        order.addItem(new Item("PS4-WITCHER", "Witcher for PS4", newBigDecimal(49.99)), 1)
             .setSubTotal(BigDecimal.ZERO);
        order.addItem(new Item("PS4-STICKER", "Customiser sticker", newBigDecimal(2.99)), 4)
             .setSubTotal(newBigDecimal(6.98));

        order.setTotal(newBigDecimal(1234.56)); // That's not the right total

        // Exec - collect result into a string
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        receiptWriter.write(order, out);
        String receipt = out.toString();

        // Assertions
        System.out.print(receipt);

        List<String> lines = Arrays.asList(receipt.split(System.lineSeparator()));
        assertThat(lines.get(0)).containsIgnoringCase("order 1542");

        linesContains(lines, "Ps4", "179.9", "199.99");
        linesContains(lines, "Customiser sticker", "1.00", "2.99");
        linesContains(lines, "Customiser sticker", "free", "2.99");
        linesContains(lines, "MGS for PS4", "49.99");
        linesContains(lines, "PS4 Remote", "19.90");
        linesContains(lines, "PS4 Remote", "free");

    }

    /** Expect 1 line contains all of this - case insensitive */
    private void linesContains(List<String> lines, String... expectedContent) {
        boolean found = lines.stream().filter(l -> {
            for (String pattern : expectedContent) {
                if (!l.toUpperCase().contains(pattern.toUpperCase())) {
                    return false;
                }
            }

            return true;
        }).findFirst().isPresent();

        assertThat(found).as("Line with patterns: " + Arrays.asList(expectedContent) + " hasn't been found").isTrue();
    }
}