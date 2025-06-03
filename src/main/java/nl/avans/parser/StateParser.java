package nl.avans.parser;

import nl.avans.declaration.Declaration;
import nl.avans.declaration.state.State;
import nl.avans.declaration.state.StateFactory;
import nl.avans.tokenizer.Token;

public class StateParser implements DeclarationParserStrategy {

    @Override
    public Declaration parse(Token[] tokens, ParsingContext ctx) {
        String identifier = tokens[1].literal();

        State declaration = StateFactory.create(tokens, ctx);

        ctx.addState(identifier, declaration);

        return declaration;
    }

}
