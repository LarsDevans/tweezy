package nl.avans.declaration.state;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.RulesetDirector;
import nl.avans.visitor.IDeclarationVisitor;

public class CompoundState extends State {

    private List<State> children = new ArrayList<>();

    public CompoundState(
        String identifier,
        String parentIdentifier,
        String name
    ) {
        super(identifier, parentIdentifier, name, "COMPOUND");
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
    public String toString() {
        return String.format(
            """
            --------------------------------------------------
            Compound State with:
                Identifier: %s
                Parent:     %s
                Name:       %s
                Children:   %s
            --------------------------------------------------
            """,
            super.getIdentifier(),
            super.getParentIdentifier(),
            getName(),
            getChildren().stream().map(State::getIdentifier).collect(Collectors.joining(", "))
        );
    }

    @Override
    public void Accept(IDeclarationVisitor visitor) {
        visitor.Visit(this);
    }
}
