package fr.dush.checkout.domain;

import lombok.*;

import java.math.BigDecimal;

/**
 * This is a relation between an Order and 1 type of item. We can have several item of this type (using #count
 * attribute), but Order should have only 1 OrderItem per type of item.
 * <p>
 * The unique business key for an OrderItem is: Order.orderId + item.itemId.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "item")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Can be used by serializer and/or persistence framework
@RequiredArgsConstructor
public class OrderItem {

    /** Item added to this order */
    @NonNull
    private Item item;

    /** Number of ordered items */
    private int number = 1;

    /** #count is including this number of free items */
    private int freeItems = 0;

    /** Discount for this group of items (of same type) */
    private BigDecimal discount = BigDecimal.ZERO;

    /** Subtotal for this group, computed by InvoicingService */
    private BigDecimal subTotal = BigDecimal.ZERO;

    public OrderItem(Item item, int number) {
        this.item = item;
        this.number = number;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }
}
