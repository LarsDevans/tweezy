package nl.avans.declaration.state;

import java.util.Optional;

import nl.avans.declaration.Action;
import nl.avans.declaration.Transition;
import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.RulesetDirector;
import nl.avans.visitor.IDeclarationVisitor;

public class InitialState extends State {

    private Transition transition;
    private Action action;

    public InitialState(
        String identifier,
        String parentIdentifier,
        String name
    ) {
        super(identifier, parentIdentifier, name, "INITIAL");
    }

    public Transition getTransition() {
        return transition;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public void attachReferences(ParsingContext ctx) {
        super.attachReferences(ctx);

        transition = ctx.where(
            ctx.getAllTransitions(),
            Transition::getSourceIdentifier,
            super.getIdentifier()
        ).orElse(null);
        action = ctx.getAction(super.getIdentifier()) == null ? null : ctx.getAction(super.getIdentifier()).getFirst();
    }

    @Override
    public void attachRuleset() {
        RulesetDirector director = new RulesetDirector();
        director.constructInitialStateRuleset(this);
        super.setRuleset(director.build());
    }

    @Override
    public String toString() {
        return String.format(
            """
            --------------------------------------------------
            Initial State with:
                Identifier: %s
                Parent:     %s
                Name:       %s
                Transition: %s
                Action:     %s
            --------------------------------------------------
            """,
            super.getIdentifier(),
            super.getParentIdentifier(),
            getName(),
            Optional.ofNullable(getTransition()).map(Transition::getIdentifier).orElse("None"),
            Optional.ofNullable(getAction()).map(Action::getIdentifier).orElse("None")
        );
    }

    @Override
    public void Accept(IDeclarationVisitor visitor) {
        visitor.Visit(this);
    }

}
