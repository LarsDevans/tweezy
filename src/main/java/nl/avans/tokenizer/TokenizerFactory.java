package nl.avans.tokenizer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import nl.avans.tokenizer.Tokenizer.ScriptLine;

// Concrete tokenizer implementation producer.
public abstract class TokenizerFactory {

    // The token keyword is mapped to a concrete tokenizer implementation constructor.
    private static final Map<String, Function<ScriptLine, Tokenizer>> registry =
        new HashMap<>();

    static {
        registry.put("STATE", StateTokenizer::new);
        registry.put("ACTION", ActionTokenizer::new);
        registry.put("TRIGGER", TriggerTokenizer::new);
        registry.put("TRANSITION", TransitionTokenizer::new);
    }

    public static Tokenizer create(String scriptLine) {
        // Every keyword is the first word within a script line.
        String keyword = scriptLine.trim().split("\\s+")[0];
        Function<ScriptLine, Tokenizer> constructor = registry.get(keyword);
        return constructor != null
            ? constructor.apply(new ScriptLine(scriptLine))
            : null;
    }
}
