package nl.avans.tokenizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Tokenizer {

    protected final String scriptLine;

    public Tokenizer(String scriptLine) {
        this.scriptLine = scriptLine;
    }

    protected abstract Pattern getPattern();

    protected abstract int getExpectedTokenCount();

    public Token[] tokenize() {
        Pattern pattern = getPattern();
        Matcher matcher = pattern.matcher(scriptLine);

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
            tokens[i] = new Token(matcher.group(i + 1));
        }
        return tokens;
    }

}
