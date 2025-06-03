package nl.avans.tokenizer;

import java.util.regex.Pattern;

public class TriggerTokenizer extends Tokenizer {

    public TriggerTokenizer(String scriptLine) {
        super(scriptLine);
    }

    @Override
    protected Pattern getPattern() {
        // Syntax: TRIGGER <identifier> "<description>";
        // For the specific ruleset, check page 6 of the assignment document.
        return Pattern.compile("(TRIGGER)\\s(\\w+)\\s\"(.+)\";");
    }

    @Override
    protected int getExpectedTokenCount() {
        return 3;
    }

}
