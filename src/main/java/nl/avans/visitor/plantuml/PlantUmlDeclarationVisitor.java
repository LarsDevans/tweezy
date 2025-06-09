package nl.avans.visitor.plantuml;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.avans.declaration.Action;
import nl.avans.declaration.Declaration;
import nl.avans.declaration.Transition;
import nl.avans.declaration.Trigger;
import nl.avans.declaration.state.CompoundState;
import nl.avans.declaration.state.FinalState;
import nl.avans.declaration.state.InitialState;
import nl.avans.declaration.state.SimpleState;
import nl.avans.visitor.IDeclarationVisitor;
import nl.avans.visitor.IDrawStrategy;

public class PlantUmlDeclarationVisitor implements IDrawStrategy, IDeclarationVisitor {

    private StringBuilder pumlBuilder;
    private Set<String> builtDeclarations;

    public PlantUmlDeclarationVisitor() {
        pumlBuilder = new StringBuilder();
        builtDeclarations = new HashSet<String>();
    }

    @Override
    public void Draw(List<Declaration> visitee) {
        pumlBuilder.append("@startuml");
        
        for (Declaration declaration : visitee) {
            if (!builtDeclarations.contains(declaration.getIdentifier())) {
                declaration.Accept(this);
            }
        }
        
        pumlBuilder.append("\n@enduml");
        System.out.println(pumlBuilder.toString());
        try {
            new PlantUmlImageFetcher(pumlBuilder.toString());
        } catch (IOException e) {
            System.out.println("Failed fetching PlantUML image with given syntax");
        }
    }

    @Override
    public void Visit(Transition visitee) {
        String source = visitee.getSourceIdentifier();
        String destination = visitee.getDestinationIdentifier();
        Trigger trigger = visitee.getTrigger();
        Action action = visitee.getAction();
        String guard = visitee.getGuardCondition();
        

        pumlBuilder.append(String.format("\n%s -> %s", source, destination));

        boolean hasTrigger = trigger != null;
        boolean hasGuard = guard != null && !guard.isEmpty();
        boolean hasAction = action != null;
        
        if (hasTrigger || hasGuard || hasAction) {
            pumlBuilder.append(" :");

            if (hasTrigger) pumlBuilder.append(String.format(" %s", trigger.getDescription()));
            if (hasGuard) pumlBuilder.append(String.format(" [%s]", guard));

            if ((hasTrigger || hasGuard) && hasAction) pumlBuilder.append(String.format(" /"));
            if (hasAction) pumlBuilder.append(String.format(" %s", action.getDescription()));
        }
        
        builtDeclarations.add(visitee.getIdentifier());
    }

    @Override
    public void Visit(CompoundState visitee) {
        pumlBuilder.append(String.format("\nstate %s as \"%s\" {", visitee.getIdentifier(), visitee.getName()));

        visitee.getChildren().forEach(child -> child.Accept(this));
        
        pumlBuilder.append("\n}");
        builtDeclarations.add(visitee.getIdentifier());
    }

    @Override
    public void Visit(FinalState visitee) {
        String identifier = visitee.getIdentifier();
        pumlBuilder.append(String.format("\nstate %s <<end>>", identifier));

        String name = visitee.getName();
        if (!name.isEmpty()) {
            pumlBuilder.append(String.format("\nnote top of %s : %s", identifier, name));
        }

        builtDeclarations.add(identifier);
    }

    @Override
    public void Visit(InitialState visitee) {
        String identifier = visitee.getIdentifier();
        pumlBuilder.append(String.format("\nstate %s <<start>>", identifier));

        String name = visitee.getName();
        if (!name.isEmpty()) {
            pumlBuilder.append(String.format("\nnote top of %s : %s", identifier, name));
        }

        builtDeclarations.add(visitee.getIdentifier());
    }

    @Override
    public void Visit(SimpleState visitee) {
        String identifier = visitee.getIdentifier();
        pumlBuilder.append(String.format("\nstate %s as \"%s\"", identifier, visitee.getName()));
        
        for (Action action : visitee.getActions()) {
            String type = switch (action.getActionType()) {
                case "ENTRY_ACTION" -> "entry";
                case "DO_ACTION"    -> "do";
                case "EXIT_ACTION"  -> "exit";
                default             -> "";
            };

            pumlBuilder.append(String.format("\n%s : %s/%s", identifier, type, action.getDescription()));
        }

        builtDeclarations.add(identifier);
    }

    @Override
    public void Visit(List<Declaration> visitee) {}

    @Override
    public void Visit(Declaration visitee) {}

    @Override
    public void Visit(Action visitee) {}

    @Override
    public void Visit(Trigger visitee) {}
    
}
