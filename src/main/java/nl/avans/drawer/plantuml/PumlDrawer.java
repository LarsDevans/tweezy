package nl.avans.drawer.plantuml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.avans.declaration.Action;
import nl.avans.declaration.Transition;
import nl.avans.declaration.Trigger;
import nl.avans.declaration.Action.ActionType;
import nl.avans.declaration.Declaration.Identifier;
import nl.avans.declaration.state.CompoundState;
import nl.avans.declaration.state.FinalState;
import nl.avans.declaration.state.InitialState;
import nl.avans.declaration.state.SimpleState;
import nl.avans.declaration.state.State;
import nl.avans.drawer.IDeclarationVisitor;
import nl.avans.drawer.IDrawStrategy;
import nl.avans.drawer.IVisitable;
import nl.avans.utils.FmsStringBuilder;

public class PumlDrawer implements IDrawStrategy, IDeclarationVisitor {

    private FmsStringBuilder fsb = new FmsStringBuilder();
    private List<Identifier> visited = new ArrayList<>();

    @Override
    public void Draw(List<IVisitable> visitee) {
        fsb.append("@startuml");

        visitee.forEach(v -> {
            if (!visited.contains(v.getIdentifier())) {
                v.Accept(this);
            }
        });

        fsb.appendLn("@enduml");

        System.out.println(fsb.toString());

        try {
            new PumlImageFetcher(fsb.toString());
        } catch (IOException e) {
            System.out.println("Failed fetching PlantUML image with given syntax.");
        }
    }

    @Override
    public void Visit(Transition visitee) {
        String sourceName = visitee.getSourceIdentifier().literal();
        String destination = visitee.getDestinationIdentifier().literal();
        Optional<Trigger> trigger = visitee.getTrigger();
        Optional<Action> action = visitee.getAction();
        String guardCondition = visitee.getGuardCondition().literal();

        boolean triggerIsPresent = trigger.isPresent();
        boolean actionIsPresent = action.isPresent();
        boolean guardConditionIsNotEmpty = !guardCondition.isEmpty();

        fsb
            .appendLn("%s -> %s", sourceName, destination)
            .appendIf(triggerIsPresent || guardConditionIsNotEmpty || actionIsPresent, " :")
            .appendIfPresent(trigger, " %s", t -> t.getDescription().literal())
            .appendIf(guardConditionIsNotEmpty, " [%s]", guardCondition)
            .appendIf((triggerIsPresent || guardConditionIsNotEmpty) && actionIsPresent, " /")
            .appendIfPresent(action, " %s", a -> a.getDescription().literal());

        visited.add(visitee.getIdentifier());
    }

    @Override
    public void Visit(CompoundState visitee) {
        String identifier = visitee.getIdentifier().literal();
        String stateName = visitee.getName().literal();
        List<State> children = visitee.getChildren();

        fsb.appendLn("state %s as \"%s\" {", identifier, stateName);

        children.forEach(c -> c.Accept(this));

        fsb.append("\n}");

        visited.add(visitee.getIdentifier());
    }

    @Override
    public void Visit(FinalState visitee) {
        String identifier = visitee.getIdentifier().literal();
        String stateName = visitee.getName().literal();

        fsb
            .appendLn("state %s <<end>>", identifier)
            .appendLnIf(!stateName.isEmpty(), "note top of %s : %s", identifier, stateName);

        visited.add(visitee.getIdentifier());
    }

    @Override
    public void Visit(InitialState visitee) {
        String identifier = visitee.getIdentifier().literal();
        String stateName = visitee.getName().literal();

        fsb
            .appendLn("state %s <<start>>", identifier)
            .appendLnIf(!stateName.isEmpty(), "note top of %s : %s", identifier, stateName);

        visited.add(visitee.getIdentifier());
    }

    @Override
    public void Visit(SimpleState visitee) {
        String identifier = visitee.getIdentifier().literal();
        String stateName = visitee.getName().literal();
        List<Action> actions = visitee.getActions();

        fsb.appendLn("state %s as \"%s\"", identifier, stateName);

        actions.forEach(a -> a.Accept(this));

        visited.add(visitee.getIdentifier());
    }

    @Override
    public void Visit(Action visitee) {
        String identifier = visitee.getIdentifier().literal();
        String actionType = visitee.getActionType().name();
        String description = visitee.getDescription().literal();

        // Every transition action type uses another drawing mechanism.
        if (visitee.getActionType() == ActionType.TRANSITION_ACTION) {
            return;
        }

        fsb.appendLn("%s : %s/%s", identifier, actionType, description);

        visited.add(visitee.getIdentifier());
    }
}
