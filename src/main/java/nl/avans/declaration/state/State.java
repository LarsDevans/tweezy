package nl.avans.declaration.state;

import java.util.ArrayList;
import java.util.List;

import nl.avans.declaration.Declaration;
import nl.avans.declaration.Transition;
import nl.avans.parser.ParsingContext;

public abstract class State extends Declaration {

    private final String identifier;
    private final String parentIdentifier;
    private final String name;
    private final String stateType;

    private State parent;

    private List<Transition> transitions = new ArrayList<>();

    public State(
        String identifier,
        String parentIdentifier,
        String name,
        String stateType
    ) {
        super(identifier);

        this.identifier = identifier;
        this.parentIdentifier = parentIdentifier;
        this.name = name;
        this.stateType = stateType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getParentIdentifier() {
        return parentIdentifier;
    }

    public State getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public String getStateType() {
        return stateType;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    @Override
    public void attachReferences(ParsingContext ctx) {
        parent = ctx.getState(parentIdentifier);

        for (Transition transition : ctx.getAllTransitions().values()) {
            if (transition.getSourceIdentifier().equals(super.getIdentifier()) || // Outgoing
                transition.getDestinationIdentifier().equals(super.getIdentifier()) // Incoming
            ) {
                this.transitions.add(transition);
            }
        }
    }

}
