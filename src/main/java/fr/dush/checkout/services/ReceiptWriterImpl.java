package fr.dush.checkout.services;

import fr.dush.checkout.domain.Order;
import fr.dush.checkout.domain.OrderItem;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import static fr.dush.checkout.tools.PriceTools.*;

/**
 * This is implementation of my understanding of "receipt with the actual price of every item and the grand total".
 * <p>
 * Item are not grouped by type, they appear on as many line there are item bought. This is to display actual price.
 */
@Service
public class ReceiptWriterImpl implements ReceiptWriter {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ReceiptWriterImpl.class);

    @Override
    public void write(Order order, OutputStream out) throws IOException {
        // Wrap, if necessary, output stream into printer.
        PrintStream writer = out instanceof PrintStream ? (PrintStream) out : new PrintStream(out);

        // Write header
        writer.println("Receipt for order " + order.getOrderId());
        writeLine(writer);

        // Write content - 1 line per item bought (not grouped by type)
        order.getItems().forEach(i -> printItem(writer, i));
        writeLine(writer);

        // Write total
        writer.println(String.format("%30s : %#7.2f", "TOTAL", order.getTotal()));

    }

    /**
     * Print a line with item name, final price and original price.
     * <p>
     * This method is more complex than expected because I planned to display 1 line per type of item, and not by
     * item.
     */
    private void printItem(PrintStream writer, OrderItem orderItem) {
        // That's the group price - computed by Invoicing service
        BigDecimal expectedFullPrice = orderItem.getSubTotal();

        for (int i = 0; i < orderItem.getNumber(); i++) {
            // Full price with formatter
            BigDecimal price = orderItem.getItem().getPrice();
            String priceFormatter = "%#7.2f";

            // Discount and gift price formatter
            BigDecimal groupPrice = orderItem.getItem().getPrice().multiply(newBigDecimal(i + 1));
            if (expectedFullPrice.compareTo(groupPrice) < 0) {
                BigDecimal discount = expectedFullPrice.subtract(groupPrice);
                if (price.compareTo(discount.abs()) > 0) {
                    // Price of this item has been reduced - discount is less than the price
                    price = price.add(discount);
                    priceFormatter = priceFormatter +
                                     String.format(" (was " + priceFormatter + ")", orderItem.getItem().getPrice());
                } else {
                    // This item is free for customer
                    priceFormatter = "   free (was " + priceFormatter + ")";
                }
            }

            writer.println(String.format("%-30s : " + priceFormatter, orderItem.getItem().getName(), price));
        }
    }

    private static void writeLine(PrintStream writer) throws IOException {
        writer.println("---------------------------------------------");
    }
}
