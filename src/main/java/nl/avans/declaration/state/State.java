package nl.avans.declaration.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.avans.declaration.Declaration;
import nl.avans.declaration.Transition;
import nl.avans.drawer.IVisitable;
import nl.avans.parser.ParsingContext;

// Every implementation is a state at its second to highest abstraction level.
public abstract class State extends Declaration implements IVisitable {

    // Can not contain a double quotation mark (").
    public record StateName(String literal) {}

    // Determines the concrete state type (used in the factory).
    public enum StateType { COMPOUND, FINAL, INITIAL, SIMPLE }

    private final Identifier parentIdentifier;
    private final StateName name;
    private final StateType stateType;

    // A state could have zero or one parent.
    private Optional<State> parent = Optional.empty();

    private List<Transition> transitions = new ArrayList<>();

    public State(
        Identifier identifier,
        Identifier parentIdentifier,
        StateName name,
        StateType stateType
    ) {
        super(identifier);

        this.parentIdentifier = parentIdentifier;
        this.name = name;
        this.stateType = stateType;
    }

    public Identifier getParentIdentifier() {
        return parentIdentifier;
    }

    public Optional<State> getParent() {
        return parent;
    }

    public StateName getName() {
        return name;
    }

    public StateType getStateType() {
        return stateType;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    @Override
    public void attachReferences(ParsingContext ctx) {
        State parent = ctx.getState(parentIdentifier);
        if (parent != null) {
            this.parent = Optional.of(parent);
        }

        for (Transition transition : ctx.getAllTransitions().values()) {
            // A transition starting from this state.
            if (transition.getSourceIdentifier().equals(super.getIdentifier())) {
                this.transitions.add(transition);
            }

            // A transition ending on this state.
            if (transition.getDestinationIdentifier().equals(super.getIdentifier())) {
                this.transitions.add(transition);
            }
        }
    }
}
