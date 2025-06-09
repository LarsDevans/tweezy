package nl.avans.visitor;

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

public class ConsoleDeclarationVisitor implements IDrawStrategy, IDeclarationVisitor {

    private StringBuilder consoleBuilder;
    private Set<String> builtDeclarations;
    private int childLevel;
    private static int indentSize = 3;

    public ConsoleDeclarationVisitor() {
        consoleBuilder = new StringBuilder();
        builtDeclarations = new HashSet<String>();
        childLevel = 0;
    }

    @Override
    public void Draw(List<Declaration> visitee) {
        consoleBuilder.append("####################################################################################################\n");
        
        for (Declaration declaration : visitee) {
            if (!builtDeclarations.contains(declaration.getIdentifier())) {
                consoleBuilder.append("\n");
                declaration.Accept(this);
            }
        }
        
        consoleBuilder.append("\n####################################################################################################\n");

        System.out.println(consoleBuilder.toString());
    }

    @Override
    public void Visit(Transition visitee) {
        String indent = " ".repeat(childLevel * indentSize);
        String destination = visitee.getDestination().getName();
        Trigger trigger = visitee.getTrigger();
        Action action = visitee.getAction();
        String guard = visitee.getGuardCondition();

        boolean hasTrigger = trigger != null;
        boolean hasGuard = guard != null && !guard.isEmpty();
        boolean hasAction = action != null;
        
        consoleBuilder.append(String.format("%s---", indent));

        if (hasTrigger || hasGuard || hasAction) {
            if (hasTrigger) { 
                consoleBuilder.append(trigger.getDescription());
                builtDeclarations.add(trigger.getIdentifier());
            }
            if (hasGuard) consoleBuilder.append(String.format(" [%s]", guard));

            if ((hasTrigger || hasGuard) && hasAction) consoleBuilder.append(String.format(" / "));
            if (hasAction) consoleBuilder.append(String.format("%s", action.getDescription()));
        }
        
        if (visitee.getDestination().getStateType().equals("FINAL")) {
            consoleBuilder.append(String.format("---> Final State (%s)\n", destination));
        } else {
            consoleBuilder.append(String.format("---> %s\n", destination));
        }
        
        builtDeclarations.add(visitee.getIdentifier());
    }

    @Override
    public void Visit(CompoundState visitee) {
        String indent = " ".repeat(childLevel * indentSize);
        consoleBuilder.append(String.format("%s====================================================================================================\n", indent));
        consoleBuilder.append(String.format("%s|| Compound State: %s\n", indent, visitee.getName()));
        consoleBuilder.append(String.format("%s----------------------------------------------------------------------------------------------------\n", indent));

        childLevel = childLevel + 1;
        visitee.getChildren().forEach(child -> {
            consoleBuilder.append("\n");
            child.Accept(this);
        });
        childLevel = childLevel - 1;
        consoleBuilder.append(String.format("\n%s====================================================================================================\n", indent));
        
        visitee.getTransitions().forEach(transition -> {
            if (transition.getSourceIdentifier().equals(visitee.getIdentifier())) {
                transition.Accept(this);
            }
        });

        builtDeclarations.add(visitee.getIdentifier());
    }

    @Override
    public void Visit(FinalState visitee) {
        String indent = " ".repeat(childLevel * indentSize);
        consoleBuilder.append(String.format("%s(O) Final State", indent));

        String name = visitee.getName();
        if (!name.isEmpty()) {
            consoleBuilder.append(String.format(" (%s)", name));
        }
        consoleBuilder.append("\n");

        builtDeclarations.add(visitee.getIdentifier());
    }

    @Override
    public void Visit(InitialState visitee) {
        String indent = " ".repeat(childLevel * indentSize);
        consoleBuilder.append(String.format("%sO Initial State", indent));

        String name = visitee.getName();
        if (!name.isEmpty()) {
            consoleBuilder.append(String.format(" (%s)", name));
        }
        consoleBuilder.append("\n");

        if (visitee.getTransition() != null) visitee.getTransition().Accept(this);

        builtDeclarations.add(visitee.getIdentifier());
    }

    @Override
    public void Visit(SimpleState visitee) {
        String identifier = visitee.getIdentifier();
        String indent = " ".repeat(childLevel * indentSize);

        consoleBuilder.append(String.format("%s----------------------------------------------------------------------------------------------------\n", indent));
        consoleBuilder.append(String.format("%s| %s\n", indent, visitee.getName()));
        consoleBuilder.append(String.format("%s----------------------------------------------------------------------------------------------------\n", indent));
        
        for (Action action : visitee.getActions()) {
            String type = switch (action.getActionType()) {
                case "ENTRY_ACTION" -> "Entry";
                case "DO_ACTION"    -> "Do";
                case "EXIT_ACTION"  -> "Exit";
                default             -> "";
            };

            consoleBuilder.append(String.format("%s| On %s / %s\n", indent, type, action.getDescription()));
        }
        consoleBuilder.append(String.format("%s----------------------------------------------------------------------------------------------------\n", indent));
        
        visitee.getTransitions().forEach(transition -> {
            if (transition.getSourceIdentifier().equals(identifier)) {
                transition.Accept(this);
            }
        });

        builtDeclarations.add(identifier);
    }

    @Override
    public void Visit(List<Declaration> visitee) {}

    @Override
    public void Visit(Action visitee) {System.out.println(visitee.getIdentifier());}

    @Override
    public void Visit(Declaration visitee) {System.out.println(visitee.getIdentifier());}

    @Override
    public void Visit(Trigger visitee) {System.out.println(visitee.getIdentifier());}
    
}
