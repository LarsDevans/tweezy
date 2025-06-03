package nl.avans.declaration;

import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.Ruleset;

public abstract class Declaration {

    private String identifier;

    private Ruleset ruleset;

    public Declaration(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Ruleset getRuleset() {
        return ruleset;
    }

    public void setRuleset(Ruleset ruleset) {
        this.ruleset = ruleset;
    }

    public abstract void attachReferences(ParsingContext ctx);

    public abstract void attachRuleset();

    @Override
    public abstract String toString();

}
