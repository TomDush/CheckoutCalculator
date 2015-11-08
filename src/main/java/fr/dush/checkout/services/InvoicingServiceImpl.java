package fr.dush.checkout.services;

import fr.dush.checkout.dao.OrderDAO;
import fr.dush.checkout.domain.Order;
import fr.dush.checkout.domain.OrderItem;
import fr.dush.checkout.services.rules.Rule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

import static fr.dush.checkout.tools.PriceTools.*;
import static java.util.stream.Collectors.*;

/**
 * Finalise an Order by computing gifts and discounts which must be applied to it, and computing the final total.
 * <p>
 * Chosen design is to have programed rules which can modify order by adding gifts or discounts. See readme for more
 * details.
 */
@Service
@Slf4j
public class InvoicingServiceImpl implements InvoicingService {

    @Autowired
    private RuleRepository ruleRepository;
    @Autowired
    private OrderDAO orderDAO;

    @Override
    public void finaliseOrder(Order order) {
        // Select candidate rules for this order
        Set<String> itemCodes =
                order.getItems().stream().map(orderItem -> orderItem.getItem().getCode()).collect(toSet());
        List<Rule> rules = ruleRepository.findRulesFor(itemCodes);

        // Apply this rules
        rules.forEach(r -> r.perform(order));

        // Compute totals
        BigDecimal total = order.getItems().stream().map(this::itemSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(round(total));

        // Save it
        orderDAO.save(order);

        log.debug("Order {} is finalised with total: {}", order.getOrderId(), order.getTotal());
    }

    /** Compute total for given type of item - save this sub-total in orderItem */
    private BigDecimal itemSubTotal(OrderItem orderItem) {
        // Total is: (Number of item - free items) * item price - discount
        BigDecimal itemNumber = newBigDecimal(orderItem.getNumber() - orderItem.getFreeItems());
        orderItem.setSubTotal(round(orderItem.getItem()
                                             .getPrice()
                                             .multiply(itemNumber)
                                             .subtract(orderItem.getDiscount())));

        return orderItem.getSubTotal();
    }

    /** Round big decimal */
    private static BigDecimal round(BigDecimal val) {
        return val.setScale(2, RoundingMode.HALF_UP);
    }
}
