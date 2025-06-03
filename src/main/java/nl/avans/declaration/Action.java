package nl.avans.declaration;

import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.RulesetDirector;

public class Action extends Declaration {

    private final String description;
    private final String actionType;

    private Declaration host;

    public Action(String identifier, String description, String actionType) {
        super(identifier);

        this.description = description;
        this.actionType = actionType;
    }

    public String getDescription() {
        return description;
    }

    public String getActionType() {
        return actionType;
    }

    public Declaration getHost() {
        return host;
    }

    @Override
    public void attachReferences(ParsingContext ctx) {
        switch (actionType) {
            case "ENTRY_ACTION":
            case "DO_ACTION":
            case "EXIT_ACTION":
                host = ctx.getState(super.getIdentifier());
                break;

            case "TRANSITION_ACTION":
                host = ctx.getTransition(super.getIdentifier());
                break;

            default:
                System.out.println("Could not find declaration to leech on");
                break;
        }
    }

    @Override
    public void attachRuleset() {
        RulesetDirector director = new RulesetDirector();
        director.constructActionRuleset(this);
        super.setRuleset(director.build());
    }

    @Override
    public String toString() {
        return String.format(
            """
            --------------------------------------------------
            Action with:
                Identifier:     %s
                Description:    %s
                Type:           %s
            --------------------------------------------------
            """,
            super.getIdentifier(),
            getDescription(),
            getActionType()
        );
    }

}
