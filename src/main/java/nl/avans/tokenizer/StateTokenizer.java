package nl.avans.tokenizer;

import java.util.regex.Pattern;

public class StateTokenizer extends Tokenizer {

    public StateTokenizer(String scriptLine) {
        super(scriptLine);
    }

    @Override
    protected Pattern getPattern() {
        // Syntax: STATE <identifier> <parent> "<name>" : <state_type>;
        // For the specific ruleset, check page 6 of the assignment document.
        return Pattern.compile(
            "(STATE)\\s(\\w+)\\s(\\w+|_)\\s\"(.*)\"\\s:\\s(INITIAL|SIMPLE|COMPOUND|FINAL);"
        );
    }

    @Override
    protected int getExpectedTokenCount() {
        return 5;
    }

}
