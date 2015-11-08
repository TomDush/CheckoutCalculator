package fr.dush.checkout.services;

import fr.dush.checkout.domain.Item;
import fr.dush.checkout.services.rules.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static fr.dush.checkout.tools.PriceTools.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class RuleRepositoryTest {

    @InjectMocks
    private RuleRepositoryImpl ruleRepository;

    private FreeItemRule remoteRule;
    private SpecialPriceRule stickersRule;
    private FreeInGroupRule gamesRule;
    private GiftRule consoleRule;

    @Before
    public void setUp() throws Exception {
        // Fill rules
        remoteRule = new FreeItemRule("PS4-REMOTE", 3, 1);
        stickersRule = new SpecialPriceRule("PS4-STICKER", 4, newBigDecimal(9.99));
        gamesRule = new FreeInGroupRule(Arrays.asList("PS4-WITCHER", "PS4-MGSV", "PS4-GTA", "PS4-DIVINITY"), 3);
        consoleRule = new GiftRule("PS4-CONSOLE", 1, new Item("PS4-REMOTE", "PS4 Remote", newBigDecimal(69.99)), 1);

        ruleRepository.add(remoteRule); // Third is free
        ruleRepository.add(stickersRule); // Instead of 4x 2.99
        ruleRepository.add(gamesRule); // 1 free if 3 bought together
        ruleRepository.add(consoleRule); // 1 free remote control for any bought console

    }

    /** Retrieve rules for only given items - should exclude stickersRule */
    @Test
    public void testRetrieveRules() throws Exception {
        // Test all canBeApplied implementation, both valid and not valid.
        List<Rule> rules = ruleRepository.findRulesFor(Arrays.asList("PS4-MGSV", "PS4-REMOTE"));
        assertThat(rules).hasSize(2).contains(remoteRule, gamesRule);

        rules = ruleRepository.findRulesFor(Arrays.asList("PS4-CONSOLE", "PS4-STICKER"));
        assertThat(rules).hasSize(2).contains(stickersRule, consoleRule);
    }

    /** Retrieve all rules */
    @Test
    public void testFindAll() throws Exception {
        List<Rule> rules = ruleRepository.findAll();

        assertThat(rules).hasSize(4).contains(remoteRule, gamesRule, stickersRule, consoleRule);
    }
}