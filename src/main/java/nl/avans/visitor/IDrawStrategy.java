package nl.avans.visitor;

import java.util.List;

import nl.avans.declaration.Declaration;

public interface IDrawStrategy {

    public void Draw(List<Declaration> visitee);
}
