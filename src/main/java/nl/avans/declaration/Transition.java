package nl.avans.declaration;

import java.util.List;
import java.util.Optional;
import nl.avans.declaration.state.State;
import nl.avans.drawer.IDeclarationVisitor;
import nl.avans.drawer.IVisitable;
import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.RulesetDirector;

// The connection between two states.
public class Transition extends Declaration implements IVisitable {

    // The guard condition between the two states (can be blank).
    public record GuardCondition(String literal) {}

    private Identifier sourceIdentifier;
    private Identifier destinationIdentifier;
    private Identifier triggerIdentifier;
    private GuardCondition guardCondition;

    private State source;
    private State destination;

    // A transition has either zero or one trigger.
    private Optional<Trigger> trigger = Optional.empty();

    // A transition has either zero or one action.
    private Optional<Action> action = Optional.empty();

    public Transition(
        Identifier identifier,
        Identifier sourceIdentifier,
        Identifier destinationIdentifier,
        Identifier triggerIdentifier,
        GuardCondition guardCondition
    ) {
        super(identifier);

        this.sourceIdentifier = sourceIdentifier;
        this.destinationIdentifier = destinationIdentifier;
        this.triggerIdentifier = triggerIdentifier;
        this.guardCondition = guardCondition;
    }

    public Identifier getSourceIdentifier() {
        return sourceIdentifier;
    }

    public State getSource() {
        return source;
    }

    public Identifier getDestinationIdentifier() {
        return destinationIdentifier;
    }

    public State getDestination() {
        return destination;
    }

    public Identifier getTriggerIdentifier() {
        return triggerIdentifier;
    }

    public Optional<Trigger> getTrigger() {
        return trigger;
    }

    public GuardCondition getGuardCondition() {
        return guardCondition;
    }

    public Optional<Action> getAction() {
        return action;
    }

    @Override
    public void attachReferences(ParsingContext ctx) {
        source = ctx.getState(sourceIdentifier);
        destination = ctx.getState(destinationIdentifier);

        Trigger trigger = ctx.getTrigger(triggerIdentifier);
        if (trigger != null) {
            this.trigger = Optional.of(trigger);
        }

        List<Action> actions = ctx.getActions(super.getIdentifier());
        if (actions != null && !actions.isEmpty()) {
            // This will ignore any additional actions that may have been
            // defined on the same transition by accident. Therefore ignoring.
            action = Optional.of(actions.getFirst());
        }
    }

    @Override
    public void attachRuleset() {
        RulesetDirector director = new RulesetDirector();
        director.constructTransitionRuleset(this);
        super.setRuleset(director.build());
    }

    @Override
    public void Accept(IDeclarationVisitor visitor) {
        visitor.Visit(this);
    }
}
