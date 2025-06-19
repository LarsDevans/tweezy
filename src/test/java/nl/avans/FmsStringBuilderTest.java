package nl.avans;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;

import nl.avans.utils.FmsStringBuilder;

public class FmsStringBuilderTest {

    @Test
    public void appendIf_appendsWhenConditionTrue() {
        // Arrange
        FmsStringBuilder builder = new FmsStringBuilder();

        // Act
        builder.appendIf(true, "True branch %d", 1);

        // Assert
        assertEquals("True branch 1", builder.toString());
    }

    @Test
    public void appendLnIfPresent_appendsNewlineWhenOptionalPresent() {
        // Arrange
        FmsStringBuilder builder = new FmsStringBuilder();

        // Act
        builder.append("Message:");
        builder.appendLnIfPresent(
            Optional.of("hello world"),
            "%s",
            s -> s.toUpperCase()
        );

        // Assert
        assertEquals("Message:\nHELLO WORLD", builder.toString());
    }
}
