package nl.avans.visitor;

import java.util.List;

import nl.avans.declaration.Action;
import nl.avans.declaration.Declaration;
import nl.avans.declaration.Transition;
import nl.avans.declaration.Trigger;
import nl.avans.declaration.state.CompoundState;
import nl.avans.declaration.state.FinalState;
import nl.avans.declaration.state.InitialState;
import nl.avans.declaration.state.SimpleState;

public interface IDeclarationVisitor {
    public void Visit(List<Declaration> visitee);
    public void Visit(Action visitee);
    public void Visit(Declaration visitee);
    public void Visit(Transition visitee);
    public void Visit(Trigger visitee);
    public void Visit(CompoundState visitee);
    public void Visit(FinalState visitee);
    public void Visit(InitialState visitee);
    public void Visit(SimpleState visitee);
}
