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
            .addRule(CommonRule.StateDeterministicRule(state), state)
            .addRule(CommonRule.DenyIncomingTransitionsRule(state), state);
    }

    public void constructFinalStateRuleset(FinalState state) {
        builder
            .addRule(CommonRule.DeclarationIdentifierRule(state), state)
            .addRule(CommonRule.StateParentalRule(state), state)
            .addRule(CommonRule.StateNamingRule(state), state)
            .addRule(CommonRule.StateDeterministicRule(state), state)
            .addRule(
                new Rule<FinalState>(
                    s -> !s.getTransitions().stream()
                        .anyMatch(t -> t.getSourceIdentifier().equals(s.getIdentifier())),
                    String.format(
                        "De state '%s' mag geen uitgaande transities bevatten.",
                        state.getIdentifier()
                    )
                ),
                state
            );
    }

    public void constructInitialStateRuleset(InitialState state) {
        builder
            .addRule(CommonRule.DeclarationIdentifierRule(state), state)
            .addRule(CommonRule.StateParentalRule(state), state)
            .addRule(CommonRule.StateNamingRule(state), state)
            .addRule(CommonRule.StateDeterministicRule(state), state)
            .addRule(CommonRule.DenyIncomingTransitionsRule(state), state);
    }

    public void constructSimpleStateRuleset(SimpleState state) {
        builder
            .addRule(CommonRule.DeclarationIdentifierRule(state), state)
            .addRule(CommonRule.StateParentalRule(state), state)
            .addRule(CommonRule.StateNamingRule(state), state)
            .addRule(CommonRule.StateDeterministicRule(state), state)
            .addRule(
                new Rule<SimpleState>(
                    s -> s.getTransitions().stream().anyMatch(
                        t -> t.getDestinationIdentifier().equals(s.getIdentifier())
                    ),
                    String.format(
                        "De state '%s' is niet bereikbaar.",
                        state.getIdentifier()
                    )
                ),
                state
            );
    }

    public void constructTriggerRuleset(Trigger trigger) {
        builder
            .addRule(CommonRule.DeclarationIdentifierRule(trigger), trigger)
            .addRule(
                new Rule<Trigger>(
                    t -> !t.getDescription().literal().isBlank(),
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
                    a -> !a.getDescription().literal().isBlank(),
                    String.format(
                        "De description van action '%s' is ongeldig. Het moet minstens uit één karakter bestaan.",
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
                s -> s.getIdentifier().literal().matches("[a-zA-Z]+"),
                String.format(
                    "De identifier '%s' is ongeldig. Alleen letters (a-z, A-Z) zijn toegestaan.",
                    declaration.getIdentifier()
                )
            );
        }

        public static Rule<State> StateParentalRule(State state) {
            return new Rule<State>(
                s -> (s.getParentIdentifier().literal().equals("_") && s.getParent().isEmpty()) ||
                     (s.getParent().isPresent() && s.getParent().get() instanceof State),
                String.format(
                    "De parent van state '%s' is ongeldig. Een state moet een geldige parent hebben of '_' als er geen parent is.",
                    state.getIdentifier()
                )
            );
        }

        public static Rule<State> StateNamingRule(State state) {
            return new Rule<State>(
                s -> !s.getName().literal().contains("\""),
                String.format(
                    "De naam van state '%s' bevat een ongeldig teken (\"). Gebruik geen aanhalingstekens in de naam.",
                    state.getIdentifier()
                )
            );
        }

        public static Rule<State> StateDeterministicRule(State state) {
            return new Rule<State>(
                s -> {
                    if (s.getTransitions().size() <= 1) {
                        return true;
                    }

                    long uniquePairs = s.getTransitions().stream()
                        .map(t ->
                            String.format(
                                "%s | %s",
                                t.getTrigger().isPresent()
                                    ? t.getTrigger().get().getDescription()
                                    : "_",
                                t.getGuardCondition()
                            )
                        )
                        .distinct()
                        .count();

                    return uniquePairs == s.getTransitions().size();
                },
                String.format(
                    "De transitions van state '%s' zijn niet deterministisch.",
                    state.getIdentifier()
                )
            );
        }

        public static Rule<State> DenyIncomingTransitionsRule(State state) {
            return new Rule<State>(
                s -> !s.getTransitions().stream()
                    .anyMatch(t -> t.getDestinationIdentifier().equals(s.getIdentifier())),
                String.format(
                    "De state '%s' mag geen inkomende transities bevatten.",
                    state.getIdentifier()
                )
            );
        }
    }
}
