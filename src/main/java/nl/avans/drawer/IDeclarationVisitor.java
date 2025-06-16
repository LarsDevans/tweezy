package nl.avans.drawer;

import nl.avans.declaration.Action;
import nl.avans.declaration.Transition;
import nl.avans.declaration.state.CompoundState;
import nl.avans.declaration.state.FinalState;
import nl.avans.declaration.state.InitialState;
import nl.avans.declaration.state.SimpleState;

public interface IDeclarationVisitor {

    public void Visit(Action visitee);

    public void Visit(Transition visitee);

    public void Visit(CompoundState visitee);

    public void Visit(FinalState visitee);

    public void Visit(InitialState visitee);

    public void Visit(SimpleState visitee);
}
