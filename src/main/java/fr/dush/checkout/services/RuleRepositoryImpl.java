package fr.dush.checkout.services;

import fr.dush.checkout.services.rules.Rule;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.*;

/** Simple implementation to store (in memory) rules and retrieve them. */
@Repository
public class RuleRepositoryImpl implements RuleRepository {

    private List<Rule> rules = new ArrayList<>();

    @Override
    public void add(Rule rule) {
        rules.add(rule);
    }

    @Override
    public List<Rule> findAll() {
        return Collections.unmodifiableList(rules);
    }

    @Override
    public List<Rule> findRulesFor(Collection<String> itemCodes) {
        return rules.stream().filter(r -> r.canBeApplied(itemCodes)).collect(toList());
    }
}
