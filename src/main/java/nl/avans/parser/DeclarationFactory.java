package nl.avans.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import nl.avans.declaration.Declaration;
import nl.avans.tokenizer.Token;

// ANCHOR - Late binding factory method
public class DeclarationFactory {

    private static final Map<
        String,
        BiFunction<Token[], ParsingContext, Declaration>
    > registry = new HashMap<>();

    static {
        registry.put("STATE", (tokens, ctx) ->
            new StateParser().parse(tokens, ctx));
        registry.put("ACTION", (tokens, ctx) ->
            new ActionParser().parse(tokens, ctx));
        registry.put("TRIGGER", (tokens, ctx) ->
            new TriggerParser().parse(tokens, ctx));
        registry.put("TRANSITION", (tokens, ctx) ->
            new TransitionParser().parse(tokens, ctx));
    }

    public static Declaration create(
        String identifier,
        Token[] tokens,
        ParsingContext context
    ) {
        BiFunction<Token[], ParsingContext, Declaration> parser =
            registry.get(identifier);
        return parser != null ? parser.apply(tokens, context) : null;
    }

}
