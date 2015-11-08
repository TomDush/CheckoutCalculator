package fr.dush.checkout.domain;

import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * Order, containing each selected items and discount if any.
 */
public class Order {

    /** Generated value */
    private Long orderId;

    /** This is the actual price of this order, including all discounts. */
    private BigDecimal total;

    /** Thanks to OrderItem equals and hashcode, we're sure we'll never have 2 OrderItem for the same item. */
    private Set<OrderItem> items = new LinkedHashSet<>();

    /**
     * Convenience method to add an item without duplicating or overriding an OrderItem with same id already in the
     * list.
     *
     * @param item   Type of item to add to this list
     * @param number Number of item of this type to add
     * @return OrderItem created (or retrieved) for this relation
     */
    public OrderItem addItem(Item item, int number) {
        Assert.isTrue(item != null && isNotEmpty(item.getCode()), "Item must no be null or have a code null.");

        // Existing order item?
        Optional<OrderItem> orderItem =
                items.stream().filter(o -> Objects.equals(item.getCode(), o.getItem().getCode())).findFirst();

        // Yes -> increment the number
        orderItem.ifPresent(o -> o.setNumber(o.getNumber() + number));

        // No -> create a new one
        return orderItem.orElseGet(() -> {
            OrderItem newItem = new OrderItem(item, number);
            items.add(newItem);
            return newItem;
        });
    }

    // ** GENERATED **

    public Stream<OrderItem> filterItems(Predicate<OrderItem> filter) {
        return items.stream().filter(filter);
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public Set<OrderItem> getItems() {
        return this.items;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }
}
