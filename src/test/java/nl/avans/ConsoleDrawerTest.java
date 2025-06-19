package nl.avans;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;

import nl.avans.declaration.Declaration.Identifier;
import nl.avans.declaration.state.FinalState;
import nl.avans.declaration.state.State.StateName;
import nl.avans.drawer.ConsoleDrawer;
import nl.avans.drawer.IVisitable;

public class ConsoleDrawerTest {

    @Test
    public void draw_drawsExpectedUML() {
        // Arrange
        ConsoleDrawer drawer = new ConsoleDrawer();
        FinalState finalState = new FinalState(
            new Identifier("final"),
            new Identifier("_"),
            new StateName("State A")
        );
        IVisitable[] visitables = { finalState };

        // Act
        drawer.Draw(List.of(visitables));

        // Assert
        assertEquals(
            drawer.getFsb().toString(),
            String.format(
                "\n%s\n\n(O) Final State (State A)\n\n%s",
                "#".repeat(100),
                "#".repeat(100)
            )
        );
    }
}
