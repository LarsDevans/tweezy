package nl.avans;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import nl.avans.tokenizer.ActionTokenizer;
import nl.avans.tokenizer.StateTokenizer;
import nl.avans.tokenizer.Token;
import nl.avans.tokenizer.Tokenizer;
import nl.avans.tokenizer.TransitionTokenizer;
import nl.avans.tokenizer.TriggerTokenizer;
import nl.avans.tokenizer.Tokenizer.ScriptLine;

@RunWith(Parameterized.class)
public class TokenizerTest {

    private ScriptLine scriptLine;
    private Class<? extends Tokenizer> tokenizerClass;
    private Token[] expectedTokens;

    public TokenizerTest(
        ScriptLine scriptLine,
        Class<? extends Tokenizer> tokenizerClass,
        Token[] expectedTokens
    ) {
        this.scriptLine = scriptLine;
        this.expectedTokens = expectedTokens;
        this.tokenizerClass = tokenizerClass;
    }

    @Parameters(name = "{index}: {1} -> {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {
                new ScriptLine("ACTION on \"Turn lamp on\" : ENTRY_ACTION;"),
                ActionTokenizer.class,
                tokens("ACTION", "on", "Turn lamp on", "ENTRY_ACTION")
            },
            {
                new ScriptLine("STATE initial _ \"powered off\" : INITIAL;"),
                StateTokenizer.class,
                tokens("STATE", "initial", "_", "powered off", "INITIAL")
            },
            {
                new ScriptLine("TRANSITION initialToOff initial -> off powerOn \"\";"),
                TransitionTokenizer.class,
                tokens("TRANSITION", "initialToOff", "initial", "off", "powerOn", "")
            },
            {
                new ScriptLine("TRIGGER powerOn \"turn power on\";"),
                TriggerTokenizer.class,
                tokens("TRIGGER", "powerOn", "turn power on")
            }
        });
    }

    private static Token[] tokens(String... values) {
        return Arrays.stream(values)
            .map(Token::new)
            .toArray(Token[]::new);
    }

    @Test
    public void tokenize_tokenizesExpectedTokens() {
        try {
            // Arrange
            Tokenizer tokenizer = tokenizerClass
                .getDeclaredConstructor(ScriptLine.class)
                .newInstance(scriptLine);

            // Act
            Token[] actualTokens = tokenizer.tokenize();

            // Assert
            assertArrayEquals(
                String.format("Failed for input: %s", scriptLine.literal()),
                expectedTokens,
                actualTokens
            );
        } catch (Exception e) {
            fail(
                String.format("Could not instantiate tokenizer %s : %s",
                tokenizerClass.getSimpleName(),
                e.getMessage())
            );
        }
    }
}
