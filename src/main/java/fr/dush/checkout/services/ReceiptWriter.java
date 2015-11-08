package fr.dush.checkout.services;

import fr.dush.checkout.domain.Order;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Thomas Duchatelle
 */
public interface ReceiptWriter {

    /** Write receipt into a stream */
    void write(Order order, OutputStream out) throws IOException;
}
