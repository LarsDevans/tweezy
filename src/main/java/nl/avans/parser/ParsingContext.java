package nl.avans.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import nl.avans.declaration.Action;
import nl.avans.declaration.Transition;
import nl.avans.declaration.Trigger;
import nl.avans.declaration.Declaration.Identifier;
import nl.avans.declaration.state.State;

public class ParsingContext {

    private Map<Identifier, State> states = new HashMap<>();
    private Map<Identifier, Trigger> triggers = new HashMap<>();
    private Map<Identifier, List<Action>> actions = new HashMap<>();
    private Map<Identifier, Transition> transitions = new HashMap<>();

    public void addState(Identifier identifier, State node) {
        states.put(identifier, node);
    }

    public Map<Identifier, State> getAllStates() {
        return states;
    }

    public State getState(Identifier identifier) {
        return states.get(identifier);
    }

    public void addTrigger(Identifier identifier, Trigger trigger) {
        triggers.put(identifier, trigger);
    }

    public Map<Identifier, Trigger> getAllTriggers() {
        return triggers;
    }

    public Trigger getTrigger(Identifier identifier) {
        return triggers.get(identifier);
    }

    public void addAction(Identifier identifier, Action action) {
        if (actions.get(identifier) != null) {
            actions.get(identifier).add(action);
        } else {
            List<Action> list = new ArrayList<Action>();
            list.add(action);
            actions.put(identifier, list);
        }
    }

    public Map<Identifier, List<Action>> getAllActions() {
        return actions;
    }

    public List<Action> getActions(Identifier identifier) {
        return actions.get(identifier);
    }

    public void addTransition(Identifier identifier, Transition transition) {
        transitions.put(identifier, transition);
    }

    public Map<Identifier, Transition> getAllTransitions() {
        return transitions;
    }

    public Transition getTransition(Identifier identifier) {
        return transitions.get(identifier);
    }

    public <T> Optional<T> where(
        Map<Identifier, T> map,
        Function<T, Identifier> identifierGetter,
        Identifier matchingId
    ) {
        return map.values().stream()
            .filter(value -> identifierGetter.apply(value).equals(matchingId))
            .findFirst();
    }
}
