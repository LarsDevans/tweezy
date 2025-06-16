package nl.avans.drawer;

import java.util.List;

public interface IDrawStrategy {

    public void Draw(List<IVisitable> visitee);
}
