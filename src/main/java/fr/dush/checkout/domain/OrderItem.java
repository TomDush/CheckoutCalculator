package fr.dush.checkout.domain;

import java.math.BigDecimal;

/**
 * This is a relation between an Order and 1 type of item. We can have several item of this type (using #count
 * attribute), but Order should have only 1 OrderItem per type of item.
 * <p>
 * The unique business key for an OrderItem is: Order.orderId + item.itemId.
 */
// Can be used by serializer and/or persistence framework
public class OrderItem {

    /** Item added to this order */
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

    public OrderItem(Item item) {
        this.item = item;
    }

    protected OrderItem() {
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public Item getItem() {
        return this.item;
    }

    public int getNumber() {
        return this.number;
    }

    public int getFreeItems() {
        return this.freeItems;
    }

    public BigDecimal getDiscount() {
        return this.discount;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setFreeItems(int freeItems) {
        this.freeItems = freeItems;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        final OrderItem other = (OrderItem) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$item = this.getItem();
        final Object other$item = other.getItem();
        if (this$item == null ? other$item != null : !this$item.equals(other$item)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $item = this.getItem();
        result = result * PRIME + ($item == null ? 0 : $item.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof OrderItem;
    }
}
