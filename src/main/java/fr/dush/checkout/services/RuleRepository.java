package fr.dush.checkout.services;

import fr.dush.checkout.services.rules.Rule;

import java.util.Collection;
import java.util.List;

/**
 * @author Thomas Duchatelle
 */
public interface RuleRepository {

    void add(Rule rule);

    List<Rule> findAll();

    List<Rule> findRulesFor(Collection<String> itemCodes);
}
