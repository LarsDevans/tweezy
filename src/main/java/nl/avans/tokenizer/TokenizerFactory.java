package nl.avans.tokenizer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// ANCHOR - Late binding factory method
public abstract class TokenizerFactory {

    private static final Map<String, Function<String, Tokenizer>> registry =
        new HashMap<>();

    static {
        registry.put("STATE", StateTokenizer::new);
        registry.put("ACTION", ActionTokenizer::new);
        registry.put("TRIGGER", TriggerTokenizer::new);
        registry.put("TRANSITION", TransitionTokenizer::new);
    }

    public static Tokenizer create(String scriptLine) {
        String keyword = scriptLine.trim().split("\\s+")[0];
        Function<String, Tokenizer> constructor = registry.get(keyword);
        return constructor != null ? constructor.apply(scriptLine) : null;
    }

}
