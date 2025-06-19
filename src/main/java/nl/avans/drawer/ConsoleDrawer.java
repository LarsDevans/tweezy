package nl.avans.drawer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.avans.declaration.Action;
import nl.avans.declaration.Transition;
import nl.avans.declaration.Trigger;
import nl.avans.declaration.Declaration.Identifier;
import nl.avans.declaration.state.CompoundState;
import nl.avans.declaration.state.FinalState;
import nl.avans.declaration.state.InitialState;
import nl.avans.declaration.state.SimpleState;
import nl.avans.declaration.state.State;
import nl.avans.utils.FmsStringBuilder;

public class ConsoleDrawer implements IDrawStrategy, IDeclarationVisitor {

    private FmsStringBuilder fsb = new FmsStringBuilder();
    private List<Identifier> visited = new ArrayList<>();
    private int childLevel = 0;
    private int indentSize = 4;

    private String getIndent() {
        return "\s".repeat(childLevel * indentSize);
    }

    public FmsStringBuilder getFsb() {
        return fsb;
    }

    @Override
    public void Draw(List<IVisitable> visitee) {
        fsb.appendLn("%s\n", "#".repeat(100));

        visitee.forEach(v -> {
            if (!visited.contains(v.getIdentifier())) {
                v.Accept(this);
            }
        });

        fsb.appendLn("%s", "#".repeat(100));

        System.err.println(fsb.toString());
    }

    @Override
    public void Visit(Action visitee) {
        String actionType = visitee.getActionType().name();
        String description = visitee.getDescription().literal();

        fsb.appendLn("%s| On %s / %s", getIndent(), actionType, description);

        visited.add(visitee.getIdentifier());
    }

    @Override
    public void Visit(Transition visitee) {
        String destinationName = visitee.getDestination().getName().literal();
        Optional<Trigger> trigger = visitee.getTrigger();
        Optional<Action> action = visitee.getAction();
        String guardCondition = visitee.getGuardCondition().literal();

        fsb
            .appendLn("%s---", getIndent())
            .appendIfPresent(trigger, "%s", t -> t.getDescription().literal())
            .appendIf(!guardCondition.isBlank(), "\s[%s]", guardCondition)
            .appendIfPresent(action, " / %s", a -> a.getDescription().literal())
            .append("--> %s", destinationName);

        visited.add(visitee.getIdentifier());
        if (trigger.isPresent()) visited.add(trigger.get().getIdentifier());
        if (action.isPresent()) visited.add(action.get().getIdentifier());
    }

    @Override
    public void Visit(CompoundState visitee) {
        String stateName = visitee.getName().literal();
        List<State> children = visitee.getChildren();
        List<Transition> transition = visitee.getTransitions();

        fsb
            .appendLn("%s%s", getIndent(), "=".repeat(100))
            .appendLn("%s|| %s", getIndent(), stateName)
            .appendLn("%s%s\n", getIndent(), "-".repeat(100));

        childLevel += 1;

        children.forEach(c -> c.Accept(this));

        childLevel -= 1;

        fsb.appendLn("%s%s", getIndent(), "=".repeat(100));

        transition.forEach(t -> {
            if (t.getSource().equals(visitee)) {
                visited.add(t.getIdentifier());
                t.Accept(this);
            }
        });

        fsb.append("\n");

        visited.add(visitee.getIdentifier());
        children.forEach(c -> visited.add(c.getIdentifier()));
    }

    @Override
    public void Visit(FinalState visitee) {
        String stateName = visitee.getName().literal();

        fsb.appendLn("%s(O) Final State (%s)\n", getIndent(), stateName);

        visited.add(visitee.getIdentifier());
    }

    @Override
    public void Visit(InitialState visitee) {
        String stateName = visitee.getName().literal();
        Transition transition = visitee.getTransition();

        fsb.appendLn("%sO Initial State (%s)", getIndent(), stateName);
        transition.Accept(this);

        fsb.append("\n");

        visited.add(visitee.getIdentifier());
    }

    @Override
    public void Visit(SimpleState visitee) {
        String stateName = visitee.getName().literal();
        List<Action> actions = visitee.getActions();
        List<Transition> transition = visitee.getTransitions();

        fsb
            .appendLn("%s%s", getIndent(), "-".repeat(100))
            .appendLn("%s| %s", getIndent(), stateName)
            .appendLn("%s%s", getIndent(), "-".repeat(100));

        actions.forEach(a -> a.Accept(this));

        fsb.appendLn("%s%s", getIndent(), "-".repeat(100));

        transition.forEach(t -> {
            if (t.getSource().equals(visitee)) {
                t.Accept(this);
            }
        });

        fsb.append("\n");

        visited.add(visitee.getIdentifier());
        actions.forEach(c -> visited.add(c.getIdentifier()));
    }
}
