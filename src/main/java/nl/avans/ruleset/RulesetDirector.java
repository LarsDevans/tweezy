package nl.avans.ruleset;

import nl.avans.declaration.Action;
import nl.avans.declaration.Transition;
import nl.avans.declaration.Trigger;
import nl.avans.declaration.state.CompoundState;
import nl.avans.declaration.state.FinalState;
import nl.avans.declaration.state.InitialState;
import nl.avans.declaration.state.SimpleState;

public class RulesetDirector {

    private RulesetBuilder builder = new RulesetBuilder();

    public void setBuilder(RulesetBuilder builder) {
        this.builder = builder;
    }

    public void constructCompoundStateRuleset(CompoundState state) {
        /* TODO */
    }

    public void constructFinalStateRuleset(FinalState transition) {
        /* TODO */
    }

    public void constructInitialStateRuleset(InitialState transition) {
        /* TODO */
    }

    public void constructSimpleStateRuleset(SimpleState state) {
        /* TODO */
    }

    public void constructActionRuleset(Action state) {
        /* TODO */
    }

    public void constructTransitionRuleset(Transition state) {
        /* TODO */
    }

    public void constructTriggerRuleset(Trigger state) {
        /* TODO */
    }

    public Ruleset build() {
        return builder.build();
    }

}
