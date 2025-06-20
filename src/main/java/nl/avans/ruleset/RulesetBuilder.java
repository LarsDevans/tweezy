package nl.avans.ruleset;

import java.util.ArrayList;
import java.util.List;

import nl.avans.declaration.Declaration;
import nl.avans.ruleset.Ruleset.RuleBinding;

public class RulesetBuilder {

    private final List<RuleBinding<? extends Declaration>> bindings = new ArrayList<>();

    public <T extends Declaration> RulesetBuilder addRule(Rule<T> rule, T declaration) {
        bindings.add(new RuleBinding<>(rule, declaration));
        return this;
    }

    public Ruleset build() {
        return new Ruleset(bindings);
    }
}
