package nl.avans.ruleset;

import nl.avans.declaration.Action;
import nl.avans.declaration.Declaration;
import nl.avans.declaration.Transition;
import nl.avans.declaration.Trigger;
import nl.avans.declaration.state.CompoundState;
import nl.avans.declaration.state.FinalState;
import nl.avans.declaration.state.InitialState;
import nl.avans.declaration.state.SimpleState;
import nl.avans.declaration.state.State;

public class RulesetDirector {

    private RulesetBuilder builder = new RulesetBuilder();

    public void setBuilder(RulesetBuilder builder) {
        this.builder = builder;
    }

    public void constructCompoundStateRuleset(CompoundState state) {
        builder
            .addRule(CommonRule.DeclarationIdentifierRule(state), state)
            .addRule(CommonRule.StateParentalRule(state), state)
            .addRule(CommonRule.StateNamingRule(state), state)
            .addRule(CommonRule.StateTypeRule(state), state);
    }

    public void constructFinalStateRuleset(FinalState state) {
        builder
            .addRule(CommonRule.DeclarationIdentifierRule(state), state)
            .addRule(CommonRule.StateParentalRule(state), state)
            .addRule(CommonRule.StateNamingRule(state), state)
            .addRule(CommonRule.StateTypeRule(state), state);
    }

    public void constructInitialStateRuleset(InitialState state) {
        builder
            .addRule(CommonRule.DeclarationIdentifierRule(state), state)
            .addRule(CommonRule.StateParentalRule(state), state)
            .addRule(CommonRule.StateNamingRule(state), state)
            .addRule(CommonRule.StateTypeRule(state), state);
    }

    public void constructSimpleStateRuleset(SimpleState state) {
        builder
            .addRule(CommonRule.DeclarationIdentifierRule(state), state)
            .addRule(CommonRule.StateParentalRule(state), state)
            .addRule(CommonRule.StateNamingRule(state), state)
            .addRule(CommonRule.StateTypeRule(state), state);
    }

    public void constructTriggerRuleset(Trigger trigger) {
        builder
            .addRule(CommonRule.DeclarationIdentifierRule(trigger), trigger)
            .addRule(
                new Rule<Trigger>(
                    t -> !t.getDescription().isBlank(),
                    String.format(
                        "De description van trigger '%s' is ongeldig. Het moet minstens uit één karakter bestaan.",
                        trigger.getIdentifier()
                    )
                ),
                trigger
            );
    }

    public void constructActionRuleset(Action action) {
        builder
            .addRule(CommonRule.DeclarationIdentifierRule(action), action)
            .addRule(
                new Rule<Action>(
                    a -> !a.getDescription().isBlank(),
                    String.format(
                        "De description van action '%s' is ongeldig. Het moet minstens uit één karakter bestaan.",
                        action.getIdentifier()
                    )
                ),
                action
            )
            .addRule(
                new Rule<Action>(
                    a -> a.getActionType().matches(
                        "ENTRY_ACTION|DO_ACTION|EXIT_ACTION|TRANSITION_ACTION"
                    ),
                    String.format(
                        "Het type van action '%s' is ongeldig. Toegestane types: ENTRY_ACTION, DO_ACTION, EXIT_ACTION, TRANSITION_ACTION.",
                        action.getIdentifier()
                    )
                ),
                action
            );
    }

    public void constructTransitionRuleset(Transition transition) {
        builder
            .addRule(CommonRule.DeclarationIdentifierRule(transition), transition)
            .addRule(
                new Rule<Transition>(
                    t -> t.getSource() instanceof State,
                    String.format(
                        "De source van transition '%s' is een niet bestaande state",
                        transition.getIdentifier()
                    )
                ),
                transition
            )
            .addRule(
                new Rule<Transition>(
                    t -> t.getDestination() instanceof State,
                    String.format(
                        "De destination van transition '%s' is een niet bestaande state",
                        transition.getIdentifier()
                    )
                ),
                transition
            );
    }

    public Ruleset build() {
        return builder.build();
    }

    private static class CommonRule {

        public static Rule<Declaration> DeclarationIdentifierRule(Declaration declaration) {
           return new Rule<Declaration>(
                s -> s.getIdentifier().matches("[a-zA-Z]+"),
                String.format(
                    "De identifier '%s' is ongeldig. Alleen letters (a-z, A-Z) zijn toegestaan.",
                    declaration.getIdentifier()
                )
            );
        }

        public static Rule<State> StateParentalRule(State state) {
            return new Rule<State>(
                s -> (s.getParentIdentifier().equals("_") && s.getParent() == null) ||
                     (s.getParent() instanceof State),
                String.format(
                    "De parent van state '%s' is ongeldig. Een state moet een geldige parent hebben of '_' als er geen parent is.",
                    state.getIdentifier()
                )
            );
        }

        public static Rule<State> StateNamingRule(State state) {
            return new Rule<State>(
                s -> !s.getName().contains("\""),
                String.format(
                    "De naam van state '%s' bevat een ongeldig teken (\"). Gebruik geen aanhalingstekens in de naam.",
                    state.getIdentifier()
                )
            );
        }

        public static Rule<State> StateTypeRule(State state) {
            return new Rule<State>(
                s -> s.getStateType().matches("INITIAL|SIMPLE|COMPOUND|FINAL"),
                String.format(
                    "Het type van state '%s' is ongeldig. Toegestane types: INITIAL, SIMPLE, COMPOUND, FINAL.",
                    state.getIdentifier()
                )
            );
        }
    }
}
