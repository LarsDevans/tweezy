package nl.avans.declaration;

import java.util.Optional;

import nl.avans.declaration.state.State;
import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.RulesetDirector;

public class Transition extends Declaration {

    private String sourceIdentifier;
    private String destinationIdentifier;
    private String triggerIdentifier;
    private String guardCondition;

    private State source;
    private State destination;
    private Trigger trigger;

    public Transition(
        String identifier,
        String sourceIdentifier,
        String destinationIdentifier,
        String triggerIdentifier,
        String guardCondition
    ) {
        super(identifier);

        this.sourceIdentifier = sourceIdentifier;
        this.destinationIdentifier = destinationIdentifier;
        this.triggerIdentifier = triggerIdentifier;
        this.guardCondition = guardCondition;
    }

    public String getSourceIdentifier() {
        return sourceIdentifier;
    }

    public State getSource() {
        return source;
    }

    public String getDestinationIdentifier() {
        return destinationIdentifier;
    }

    public State getDestination() {
        return destination;
    }

    public String getTriggerIdentifier() {
        return triggerIdentifier;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public String getGuardCondition() {
        return guardCondition;
    }

    @Override
    public void attachReferences(ParsingContext ctx) {
        source = ctx.getState(sourceIdentifier);
        destination = ctx.getState(destinationIdentifier);
        trigger = ctx.getTrigger(triggerIdentifier);
    }

    @Override
    public void attachRuleset() {
        RulesetDirector director = new RulesetDirector();
        director.constructTransitionRuleset(this);
        super.setRuleset(director.build());
    }

    @Override
    public String toString() {
        return String.format(
            """
            --------------------------------------------------
            Transition with:
                Identifier:     %s
                Source:         %s
                Destination:    %s
                Trigger:        %s
                Guard:          %s
            --------------------------------------------------
            """,
            super.getIdentifier(),
            Optional.ofNullable(getSource()).map(State::getIdentifier).orElse("None"),
            Optional.ofNullable(getDestination()).map(State::getIdentifier).orElse("None"),
            Optional.ofNullable(getTrigger()).map(Trigger::getIdentifier).orElse("None"),
            getGuardCondition()
        );
    }

}
