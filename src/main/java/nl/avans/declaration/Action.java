package nl.avans.declaration;

import nl.avans.drawer.IDeclarationVisitor;
import nl.avans.drawer.IVisitable;
import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.RulesetDirector;

// A state or transition can have a causation action.
public class Action extends Declaration implements IVisitable {

    public record ActionDescription(String literal) {}

    // Defines the action characteristic and the host implicitly.
    public enum ActionType { ENTRY_ACTION, DO_ACTION, EXIT_ACTION, TRANSITION_ACTION }

    private final ActionDescription description;
    private final ActionType actionType;

    // Every action lives in either a state or transition.
    private Declaration host;

    public Action(
        Identifier identifier,
        ActionDescription description,
        ActionType actionType
    ) {
        super(identifier);

        this.description = description;
        this.actionType = actionType;
    }

    public ActionDescription getDescription() {
        return description;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Declaration getHost() {
        return host;
    }

    @Override
    public void attachReferences(ParsingContext ctx) {
        switch (actionType) {
            case ENTRY_ACTION:
            case DO_ACTION:
            case EXIT_ACTION:
                host = ctx.getState(super.getIdentifier());
                break;

            case TRANSITION_ACTION:
                host = ctx.getTransition(super.getIdentifier());
                break;

            // The ruleset will invalidate the object with a invalid reference.
            default: break;
        }
    }

    @Override
    public void attachRuleset() {
        RulesetDirector director = new RulesetDirector();
        director.constructActionRuleset(this);
        super.setRuleset(director.build());
    }

    @Override
    public void Accept(IDeclarationVisitor visitor) {
        visitor.Visit(this);
    }
}
