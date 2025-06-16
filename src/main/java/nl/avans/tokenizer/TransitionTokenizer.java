package nl.avans.tokenizer;

import java.util.regex.Pattern;

public class TransitionTokenizer extends Tokenizer {

    public TransitionTokenizer(ScriptLine scriptLine) {
        super(scriptLine);
    }

    @Override
    protected Pattern getPattern() {
        // Syntax: TRANSITION <identifier> <source_state_identifier> -> <destination_state_identifier> [<trigger_identifier>] "<guard_condition>";
        // For the specific ruleset, check page 6 of the assignment document.
        return Pattern.compile(
            "(TRANSITION)\\s(.+)\\s(\\w+)\\s->\\s(\\w+)\\s(\\w*)\\s?\"(.*)\";"
        );
    }

    @Override
    protected int getExpectedTokenCount() {
        return 6;
    }
}
