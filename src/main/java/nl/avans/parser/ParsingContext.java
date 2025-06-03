package nl.avans.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import nl.avans.declaration.Action;
import nl.avans.declaration.Transition;
import nl.avans.declaration.Trigger;
import nl.avans.declaration.state.State;

public class ParsingContext {

    private Map<String, State> states = new HashMap<>();
    private Map<String, Trigger> triggers = new HashMap<>();
    private Map<String, Action> actions = new HashMap<>();
    private Map<String, Transition> transitions = new HashMap<>();

    public void addState(String identifier, State node) {
        states.put(identifier, node);
    }

    public Map<String, State> getAllStates() {
        return states;
    }

    public State getState(String identifier) {
        return states.get(identifier);
    }

    public void addTrigger(String identifier, Trigger trigger) {
        triggers.put(identifier, trigger);
    }

    public Map<String, Trigger> getAllTriggers() {
        return triggers;
    }

    public Trigger getTrigger(String identifier) {
        return triggers.get(identifier);
    }

    public void addAction(String identifier, Action action) {
        actions.put(identifier, action);
    }

    public Map<String, Action> getAllActions() {
        return actions;
    }

    public Action getAction(String identifier) {
        return actions.get(identifier);
    }

    public void addTransition(String identifier, Transition transition) {
        transitions.put(identifier, transition);
    }

    public Map<String, Transition> getAllTransitions() {
        return transitions;
    }

    public Transition getTransition(String identifier) {
        return transitions.get(identifier);
    }

    public <T> Optional<T> where(Map<String, T> map, Function<T, String> identifierGetter, String matchingId) {
        return map.values().stream()
            .filter(value -> identifierGetter.apply(value).equals(matchingId))
            .findFirst();
    }

}
