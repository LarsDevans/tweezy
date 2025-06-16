package nl.avans.declaration.state;

import java.util.List;
import java.util.Optional;
import nl.avans.declaration.Action;
import nl.avans.declaration.Transition;
import nl.avans.drawer.IDeclarationVisitor;
import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.RulesetDirector;

// A state which denotes the end of a Tweezy diagram.
public class FinalState extends State {

    private Transition transition;

    // A final state has either zero or one action.
    private Optional<Action> action = Optional.empty();

    public FinalState(
        Identifier identifier,
        Identifier parentIdentifier,
        StateName name
    ) {
        super(identifier, parentIdentifier, name, StateType.FINAL);
    }

    public Transition getTransition() {
        return transition;
    }

    public Optional<Action> getAction() {
        return action;
    }

    @Override
    public void attachReferences(ParsingContext ctx) {
        super.attachReferences(ctx);

        // A final state can only have a incoming transition, therefore we only search
        // for a transition with the same destination identifier as the final state.
        transition = ctx.where(
            ctx.getAllTransitions(),
            Transition::getDestinationIdentifier,
            super.getIdentifier()
        ).orElse(null);

        List<Action> actions = ctx.getActions(super.getIdentifier());
        if (actions != null && !actions.isEmpty()) {
            // This will ignore any additional actions that may have been
            // defined on the same state by accident. Therefore ignoring.
            action = Optional.of(actions.getFirst());
        }
    }

    @Override
    public void attachRuleset() {
        RulesetDirector director = new RulesetDirector();
        director.constructFinalStateRuleset(this);
        super.setRuleset(director.build());
    }

    @Override
    public void Accept(IDeclarationVisitor visitor) {
        visitor.Visit(this);
    }
}
