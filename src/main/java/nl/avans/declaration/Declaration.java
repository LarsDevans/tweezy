package nl.avans.declaration;

import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.Ruleset;

// Every Tweezy object is a declaration at its highest abstraction level.
public abstract class Declaration {

    // Every declaration is identified by a unique identifier.
    public record Identifier(String literal) {}

    private Identifier identifier;
    private Ruleset ruleset;

    public Declaration(Identifier identifier) {
        this.identifier = identifier;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Ruleset getRuleset() {
        return ruleset;
    }

    public void setRuleset(Ruleset ruleset) {
        this.ruleset = ruleset;
    }

    // This declaration can have declaration references attached to it.
    public abstract void attachReferences(ParsingContext ctx);

    // This declaration must adhere to some rules (incl. its references).
    public abstract void attachRuleset();
}
