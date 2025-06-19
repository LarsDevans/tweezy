package nl.avans;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import nl.avans.declaration.Action;
import nl.avans.declaration.Declaration;
import nl.avans.declaration.Transition;
import nl.avans.declaration.Trigger;
import nl.avans.declaration.Declaration.Identifier;
import nl.avans.parser.DeclarationFactory;
import nl.avans.parser.ParsingContext;
import nl.avans.tokenizer.Token;

@RunWith(Parameterized.class)
public class ParserTest {

    private Identifier identifier;
    private Token[] tokens;
    private Class<? extends Declaration> expectedDeclaration;

    public ParserTest(
        Identifier identifier,
        Token[] tokens,
        Class<? extends Declaration> expectedDeclaration
    ) {
        this.identifier = identifier;
        this.tokens = tokens;
        this.expectedDeclaration = expectedDeclaration;
    }

    @Parameters(name = "{index}: {1} -> {0}")
    public static Collection<Object[]> data() {
        // State declaration has its own internal factory, therefore excluding.
        return Arrays.asList(new Object[][] {
            {
                new Identifier("ACTION"),
                tokens("ACTION", "on", "Turn lamp on", "ENTRY_ACTION"),
                Action.class
            },
            {
               new Identifier("TRANSITION"),
                tokens("TRANSITION", "initialToOff", "initial", "off", "powerOn", ""),
                Transition.class
            },
            {
                new Identifier("TRIGGER"),
                tokens("TRIGGER", "powerOn", "turn power on"),
                Trigger.class
            }
        });
    }

    private static Token[] tokens(String... values) {
        return Arrays.stream(values)
            .map(Token::new)
            .toArray(Token[]::new);
    }

    @Test
    public void parse_parsersExpectedDeclaration() {
        // Arrange
        ParsingContext ctx = new ParsingContext();

        // Act
        Declaration declaration = DeclarationFactory.create(identifier.literal(), tokens, ctx);

        // Assert
        assertEquals(
            String.format("Failed for input: %s", declaration.getClass().getSimpleName()),
            expectedDeclaration,
            declaration.getClass()
        );
    }
}
