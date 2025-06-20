package nl.avans;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;

import nl.avans.utils.FmsStringBuilder;

public class FmsStringBuilderTest {

    @Test
    public void appendLnIf_appendsWhenConditionTrue() {
        // Arrange
        FmsStringBuilder builder = new FmsStringBuilder();

        // Act
        builder.append("Message:");
        builder.appendLnIf(true, "Hello, World!");

        // Assert
        assertEquals("Message:\nHello, World!", builder.toString());
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
