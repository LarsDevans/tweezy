package nl.avans.declaration.state;

import java.util.ArrayList;
import java.util.List;
import nl.avans.drawer.IDeclarationVisitor;
import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.RulesetDirector;

// A state which itself can be a parental state of other states.
public class CompoundState extends State {

    // Can hold other compound states, or 'regular' states.
    private List<State> children = new ArrayList<>();

    public CompoundState(
        Identifier identifier,
        Identifier parentIdentifier,
        StateName name
    ) {
        super(identifier, parentIdentifier, name, StateType.COMPOUND);
    }

    public List<State> getChildren() {
        return children;
    }

    @Override
    public void attachReferences(ParsingContext ctx) {
        super.attachReferences(ctx);

        for (State state : ctx.getAllStates().values()) {
            if (state.getParentIdentifier().equals(super.getIdentifier())) {
                children.add(state);
            }
        }
    }

    @Override
    public void attachRuleset()  {
        RulesetDirector director = new RulesetDirector();
        director.constructCompoundStateRuleset(this);
        super.setRuleset(director.build());
    }

    @Override
    public void Accept(IDeclarationVisitor visitor) {
        visitor.Visit(this);
    }
}
