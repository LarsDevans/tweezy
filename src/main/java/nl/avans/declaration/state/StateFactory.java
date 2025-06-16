package nl.avans.declaration.state;

import java.util.HashMap;
import java.util.Map;
import nl.avans.declaration.Declaration.Identifier;
import nl.avans.declaration.state.State.StateName;
import nl.avans.declaration.state.State.StateType;
import nl.avans.parser.ParsingContext;
import nl.avans.tokenizer.Token;

// The producer of concrete state implementations.
public class StateFactory {

    // A functional interface that supports four parameter functions.
    @FunctionalInterface
    public interface TriFunction<A, B, C, R> {
        R apply(A a, B b, C c);
    }

    private static final Map<
        StateType,
        TriFunction<Identifier, Identifier, StateName, State>
    > registry = new HashMap<>();

    static {
        registry.put(StateType.SIMPLE, SimpleState::new);
        registry.put(StateType.INITIAL, InitialState::new);
        registry.put(StateType.FINAL, FinalState::new);
        registry.put(StateType.COMPOUND, CompoundState::new);
    }

    public static State create(Token[] tokens, ParsingContext ctx) {
        StateType stateType = StateType.valueOf(tokens[4].literal());
        Identifier identifier = new Identifier(tokens[1].literal());
        Identifier parent = new Identifier(tokens[2].literal());
        StateName name = new StateName(tokens[3].literal());

        TriFunction<Identifier, Identifier, StateName, State> parser =
            registry.get(stateType);

        // The state type is already defined by the enumeration. Therefore excluding.
        return parser != null ? parser.apply(identifier, parent, name) : null;
    }
}
