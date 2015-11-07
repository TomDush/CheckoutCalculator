package fr.dush.checkout.dao;

import fr.dush.checkout.domain.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static fr.dush.checkout.tools.PriceTools.*;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemDAOImplTest {

    /** Mockito will create a new instance for each test */
    @InjectMocks
    private ItemDAOImpl itemDAO;

    @Test
    public void testSave() throws Exception {
        assertThat(itemDAO.findByCode("ABC").isPresent()).isFalse();

        itemDAO.save(new Item("ABC", "Test item", newBigDecimal(42.12)));

        assertThat(itemDAO.findByCode("ABC").isPresent()).isTrue();
        Item abc = itemDAO.findByCode("ABC").get();
        assertThat(abc.getPrice()).isEqualTo(newBigDecimal(42.12));
    }

    @Test
    public void testFindAll() throws Exception {
        itemDAO.save(new Item("A1", "item1", newBigDecimal(1.2)));
        itemDAO.save(new Item("B3", "item4", newBigDecimal(2.3)));
        itemDAO.save(new Item("A2", "item2", newBigDecimal(3.4)));
        itemDAO.save(new Item("AA", "item3", newBigDecimal(4.5)));

        List<Item> items = itemDAO.findAll();
        assertThat(items).hasSize(4);

        assertThat(items.stream().map(Item::getName).collect(toList())).containsExactly("item1",
                                                                                        "item2",
                                                                                        "item3",
                                                                                        "item4");
    }
}