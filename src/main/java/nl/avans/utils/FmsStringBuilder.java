package nl.avans.utils;

import java.util.Optional;
import java.util.function.Function;

public class FmsStringBuilder {

    private final StringBuilder sb = new StringBuilder();

    public FmsStringBuilder append(String format, Object... args) {
        sb.append(String.format(format, args));
        return this;
    }

    public FmsStringBuilder appendLn(String format, Object... args) {
        return append("\n" + format, args);
    }

    public FmsStringBuilder appendIf(boolean condition, String format, Object... args) {
        if (condition) {
            return append(format, args);
        }
        return this;
    }

    public FmsStringBuilder appendLnIf(boolean condition, String format, Object... args) {
        if (condition) {
            return appendLn(format, args);
        }
        return this;
    }

    public <T> FmsStringBuilder appendIfPresent(
        Optional<T> optional,
        String format,
        Function<T, ?> argMapper
    ) {
        optional
            .map(argMapper)
            .ifPresent(arg -> sb.append(String.format(format, arg)));
        return this;
    }

    public <T> FmsStringBuilder appendLnIfPresent(
        Optional<T> optional,
        String format,
        Function<T, ?> argMapper
    ) {
        return appendIfPresent(optional, "\n" + format, argMapper);
    }

    public String toString() {
        return sb.toString();
    }
}
