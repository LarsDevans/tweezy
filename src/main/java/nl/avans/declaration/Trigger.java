package nl.avans.declaration;

import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.RulesetDirector;

// A transition can have a trigger that sets it off.
public class Trigger extends Declaration {

    public record TriggerDescription(String literal) {}

    private Transition transition;
    private TriggerDescription description;

    // The trigger its identifier is identical to the identifier of the transition.
    public Trigger(Identifier identifier, TriggerDescription description) {
        super(identifier);

        this.description = description;
    }

    public Transition getTransition() {
        return transition;
    }

    public TriggerDescription getDescription() {
        return description;
    }

    @Override
    public void attachReferences(ParsingContext ctx) {
        transition = ctx.where(
            ctx.getAllTransitions(),
            Transition::getTriggerIdentifier,
            super.getIdentifier()
        ).orElse(null);
    }

    @Override
    public void attachRuleset() {
        RulesetDirector director = new RulesetDirector();
        director.constructTriggerRuleset(this);
        super.setRuleset(director.build());
    }
}
