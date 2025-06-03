package nl.avans.declaration.state;

import java.util.HashMap;
import java.util.Map;

import nl.avans.parser.ParsingContext;
import nl.avans.tokenizer.Token;

public class StateFactory {

    @FunctionalInterface
    public interface TriFunction<A, B, C, R> {
        R apply(A a, B b, C c);
    }

    private static final Map<
        String,
        TriFunction<String, String, String, State>
    > registry =
        new HashMap<>();

    static {
        registry.put("SIMPLE", SimpleState::new);
        registry.put("INITIAL", InitialState::new);
        registry.put("FINAL", FinalState::new);
        registry.put("COMPOUND", CompoundState::new);
    }

    public static State create(Token[] tokens, ParsingContext ctx) {
        String stateType = tokens[4].literal();
        String identifier = tokens[1].literal();
        String parent = tokens[2].literal();
        String name = tokens[3].literal();

        TriFunction<String, String, String, State> parser =
            registry.get(stateType);

        return parser != null ? parser.apply(identifier, parent, name) : null;
    }

}
