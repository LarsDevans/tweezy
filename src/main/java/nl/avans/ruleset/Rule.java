package nl.avans.ruleset;

import java.util.function.Predicate;

import nl.avans.declaration.Declaration;

public class Rule<T extends Declaration> {

    private final Predicate<T> condition;
    private final String errorMessage;

    public Rule(Predicate<T> condition, String errorMessage) {
        this.condition = condition;
        this.errorMessage = errorMessage;
    }

    public boolean validate(T declaration) throws Exception {
        return condition.test(declaration);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
