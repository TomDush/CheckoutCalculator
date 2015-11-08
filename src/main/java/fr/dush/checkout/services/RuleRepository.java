package fr.dush.checkout.services;

import fr.dush.checkout.services.rules.Rule;

import java.util.Collection;
import java.util.List;

public interface RuleRepository {

    /** Register a new rule */
    void add(Rule rule);

    /** Find all registered rules */
    List<Rule> findAll();

    /** Find all registered rules which apply on at least one of this item code. */
    List<Rule> findRulesFor(Collection<String> itemCodes);
}
