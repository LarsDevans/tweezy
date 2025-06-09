package nl.avans.declaration;

import java.util.Optional;

import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.RulesetDirector;
import nl.avans.visitor.IDeclarationVisitor;

public class Trigger extends Declaration {

    // The Trigger's identifier matches the identifier of the transition, so
    // there is no need to store the Transition's identifier separately.
    private Transition transition;
    private String description;

    public Trigger(String identifier, String description) {
        super(identifier);

        this.description = description;
    }

    public Transition getTransition() {
        return transition;
    }

    public String getDescription() {
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

    @Override
    public String toString() {
        return String.format(
            """
            --------------------------------------------------
            Trigger with:
                Identifier:     %s
                Description:    %s
                Transition:     %s
            --------------------------------------------------
            """,
            super.getIdentifier(),
            getDescription(),
            Optional.ofNullable(getTransition()).map(Transition::getIdentifier).orElse("None")
        );
    }

    @Override
    public void Accept(IDeclarationVisitor visitor) {
        visitor.Visit(this);
    }

}
