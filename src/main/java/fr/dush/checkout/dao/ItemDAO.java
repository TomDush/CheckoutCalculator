package fr.dush.checkout.dao;

import fr.dush.checkout.domain.Item;

import java.util.List;
import java.util.Optional;

/**
 * Data access for items. This interface match with spring data generic interface on purpose to make improvement
 * easier.
 */
public interface ItemDAO {

    /** Save and return an item (will override any item with same code already existing */
    Item save(Item item);

    /** This signature do not exist on spring data but can be generated automatically */
    Optional<Item> findByCode(String code);

    /** Get all stored items */
    List<Item> findAll();
}
