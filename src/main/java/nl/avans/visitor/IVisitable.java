package nl.avans.visitor;

public interface IVisitable {
    public void Accept(IDeclarationVisitor visitor);
}
