package nl.avans.declaration.state;

import java.util.List;
import java.util.Optional;
import nl.avans.declaration.Action;
import nl.avans.declaration.Transition;
import nl.avans.drawer.IDeclarationVisitor;
import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.RulesetDirector;

// A state which denotes the start of a Tweezy diagram.
public class InitialState extends State {

    private Transition transition;

    // A final state has either zero or one action.
    private Optional<Action> action = Optional.empty();

    public InitialState(
        Identifier identifier,
        Identifier parentIdentifier,
        StateName name
    ) {
        super(identifier, parentIdentifier, name, StateType.INITIAL);
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

        // A initial state can only have a outgoing transition, therefore we only search
        // for a transition with the same destination identifier as the initial state.
        transition = ctx.where(
            ctx.getAllTransitions(),
            Transition::getSourceIdentifier,
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
        director.constructInitialStateRuleset(this);
        super.setRuleset(director.build());
    }

    @Override
    public void Accept(IDeclarationVisitor visitor) {
        visitor.Visit(this);
    }
}
