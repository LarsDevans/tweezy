package nl.avans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import org.junit.Test;

import nl.avans.declaration.Declaration;
import nl.avans.declaration.Declaration.Identifier;
import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.Rule;
import nl.avans.ruleset.Ruleset;
import nl.avans.ruleset.RulesetBuilder;

public class RulesetTest {

    @Test
    public void validate_validatesRuleset() {
        try {
            // Arrange
            RulesetBuilder builder = new RulesetBuilder();
            Dummy dummy = new Dummy(new Identifier("dummy"));
            Ruleset ruleset = builder.addRule(
                new Rule<Dummy>(
                    d -> true,
                    "SUCCESS: Validation passed"
                ),
                dummy
            ).build();

            // Act
            ruleset.validate();

            // Assert
            // If we reach here, it means validation passed, so we assert that nothing went wrong.
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void validate_invalidatesRuleset() {
        // Arrange
        RulesetBuilder builder = new RulesetBuilder();
        Dummy dummy = new Dummy(new Identifier("dummy"));
        Ruleset ruleset = builder.addRule(
            new Rule<Dummy>(
                d -> false,
                "ERROR: Validation failed"
            ),
            dummy
        ).build();

        // Assert
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> {
            // Act
            ruleset.validate();
        });

        assertEquals(
            "The relationships between declarations are invalid or inconsistent. The interpreter was unable to determine the specific cause. Please review the logical connections between states, transitions, and other declarations.",
            e.getMessage()
        );
    }

    private class Dummy extends Declaration {

        public Dummy(Identifier identifier) {
            super(identifier);
        }

        public void attachReferences(ParsingContext ctx) {}

        public void attachRuleset() {}
    }
}
