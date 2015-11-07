package fr.dush.checkout.dao;

import fr.dush.checkout.domain.Item;
import fr.dush.checkout.domain.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static fr.dush.checkout.tools.PriceTools.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderDAOImplTest {

    @InjectMocks
    private OrderDAOImpl orderDAO;

    @Test
    public void testSave() throws Exception {
        Order order = new Order();
        Item batMobile = new Item("PS4-CONSOLE", "Console", newBigDecimal(12.56));
        order.addItem(batMobile, 1);
        order.addItem(batMobile, 1);
        order.addItem(new Item("XBOX-CONSOLE", "Another console", newBigDecimal(8.99)), 3);

        // Exec
        Order saved = orderDAO.save(order);

        // Test id generation
        assertThat(saved.getOrderId()).isGreaterThanOrEqualTo(1L);

    }
}