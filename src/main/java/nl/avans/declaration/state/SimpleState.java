package nl.avans.declaration.state;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nl.avans.declaration.Action;
import nl.avans.declaration.Transition;
import nl.avans.parser.ParsingContext;
import nl.avans.ruleset.RulesetDirector;
import nl.avans.visitor.IDeclarationVisitor;

public class SimpleState extends State {

    private List<Action> actions = new ArrayList<>();

    public SimpleState(
        String identifier,
        String parentIdentifier,
        String name
    ) {
        super(identifier, parentIdentifier, name, "SIMPLE");
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
    public String toString() {
        return String.format(
            """
            --------------------------------------------------
            Simple State with:
                Identifier:     %s
                Parent:         %s
                Name:           %s
                Transitions:    %s
                Actions:        %s
            --------------------------------------------------
            """,
            super.getIdentifier(),
            super.getParentIdentifier(),
            getName(),
            getTransitions().stream().map(Transition::getIdentifier).collect(Collectors.joining(", ")),
            getActions().stream().map(Action::getIdentifier).collect(Collectors.joining(", "))
        );
    }

    @Override
    public void Accept(IDeclarationVisitor visitor) {
        visitor.Visit(this);
    }

}
