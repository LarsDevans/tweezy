package nl.avans.declaration.state;

import java.util.ArrayList;
import java.util.List;

import nl.avans.declaration.Action;
import nl.avans.drawer.IDeclarationVisitor;
import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.RulesetDirector;

// The most common state in a Tweezy diagram.
public class SimpleState extends State {

    private List<Action> actions = new ArrayList<>();

    public SimpleState(
        Identifier identifier,
        Identifier parentIdentifier,
        StateName name
    ) {
        super(identifier, parentIdentifier, name, StateType.SIMPLE);
    }

    public List<Action> getActions() {
        return actions;
    }

    @Override
    public void attachReferences(ParsingContext ctx) {
        super.attachReferences(ctx);

        for (List<Action> list : ctx.getAllActions().values()) {
            for (Action action : list) {
                if (action.getIdentifier().equals(super.getIdentifier())) {
                    this.actions.add(action);
                }
            }
        }
    }

    @Override
    public void attachRuleset() {
        RulesetDirector director = new RulesetDirector();
        director.constructSimpleStateRuleset(this);
        super.setRuleset(director.build());
    }

    @Override
    public void Accept(IDeclarationVisitor visitor) {
        visitor.Visit(this);
    }
}
