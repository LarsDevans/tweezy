package nl.avans.tokenizer;

import java.util.regex.Pattern;

public class ActionTokenizer extends Tokenizer {

    public ActionTokenizer(ScriptLine scriptLine) {
        super(scriptLine);
    }

    @Override
    protected Pattern getPattern() {
        // Syntax: ACTION <identifier> "<description>" : <action_type>;
        // For the specific ruleset, check page 6 of the assignment document.
        return Pattern.compile(
            "(ACTION)\\s(\\w+)\\s\"(.+)\"\\s:\\s(ENTRY_ACTION|DO_ACTION|EXIT_ACTION|TRANSITION_ACTION);"
        );
    }

    @Override
    protected int getExpectedTokenCount() {
        return 4;
    }
}
