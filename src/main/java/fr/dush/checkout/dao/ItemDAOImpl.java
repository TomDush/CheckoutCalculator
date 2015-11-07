package fr.dush.checkout.dao;

import fr.dush.checkout.domain.Item;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * In-memory implementation because this is not part of this code sample. Note that interface match with Spring-JPA if
 * we want to improve this software.
 */
@Repository
public class ItemDAOImpl implements ItemDAO {

    private Map<String, Item> items = new HashMap<>();

    @Override
    public Item save(Item item) {
        Assert.isTrue(isNotEmpty(item.getCode()), "Item code is not automatically generated, it must be provided.");
        items.put(item.getCode(), item);

        return item;
    }

    @Override
    public Optional<Item> findByCode(String code) {
        return Optional.ofNullable(items.get(code));
    }

    @Override
    public List<Item> findAll() {
        ArrayList<Item> list = new ArrayList<>(items.values());
        Collections.sort(list, (i1, i2) -> i1.getCode().compareTo(i2.getCode()));

        return list;
    }
}
