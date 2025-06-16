package nl.avans.drawer;

import nl.avans.declaration.Declaration.Identifier;

public interface IVisitable {

    public Identifier getIdentifier();

    public void Accept(IDeclarationVisitor visitor);
}
