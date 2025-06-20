package nl.avans.ruleset;

import java.io.IOException;
import java.util.List;

import nl.avans.declaration.Declaration;

public class Ruleset {

    private final List<RuleBinding<? extends Declaration>> rules;

    public Ruleset(List<RuleBinding<? extends Declaration>> rules) {
        this.rules = rules;
    }

    public void validate() throws IOException {
        for (RuleBinding<? extends Declaration> binding : rules) {
            binding.validate();
        }
    }

    public static class RuleBinding<T extends Declaration> {

        private final Rule<T> rule;
        private final T declaration;

        public RuleBinding(Rule<T> rule, T declaration) {
            this.rule = rule;
            this.declaration = declaration;
        }

        public void validate() throws IOException {
            try {
                if (!rule.validate(declaration)) {
                    throw new IllegalStateException(rule.getErrorMessage());
                }
            } catch (IllegalStateException e) {
                throw new IllegalStateException(
                    "The relationships between declarations are invalid or inconsistent. The interpreter was unable to determine the specific cause. Please review the logical connections between states, transitions, and other declarations."
                );
            }
        }

    }

}
