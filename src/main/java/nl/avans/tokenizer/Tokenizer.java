package nl.avans.tokenizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Every token implementation is a token at its highest abstraction level.
public abstract class Tokenizer {

    public record ScriptLine(String literal) {}

    protected final ScriptLine scriptLine;

    public Tokenizer(ScriptLine scriptLine) {
        this.scriptLine = scriptLine;
    }

    public Token[] tokenize() {
        Pattern pattern = getPattern();
        Matcher matcher = pattern.matcher(scriptLine.literal());

        if (!matcher.find()) {
            throw new IllegalArgumentException(
                "Invalid syntax in script line: " + scriptLine
            );
        }

        int receivedTokenCount = matcher.groupCount();
        if (receivedTokenCount != getExpectedTokenCount()) {
            throw new IllegalStateException(
                String.format(
                    "Expected %d tokens, but got %d",
                    getExpectedTokenCount(), receivedTokenCount
                )
            );
        }

        Token[] tokens = new Token[getExpectedTokenCount()];
        for (int i = 0; i < getExpectedTokenCount(); i++) {
            // Group starts at 1, therefore + 1.
            tokens[i] = new Token(matcher.group(i + 1));
        }
        return tokens;
    }

    // The pattern that get token groups from the raw statement.
    protected abstract Pattern getPattern();

    // The amount of expected token groups in a pattern.
    protected abstract int getExpectedTokenCount();
}
