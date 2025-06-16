package nl.avans.parser;

import nl.avans.declaration.Declaration;
import nl.avans.declaration.Declaration.Identifier;
import nl.avans.declaration.state.State;
import nl.avans.declaration.state.StateFactory;
import nl.avans.tokenizer.Token;

public class StateParser implements DeclarationParserStrategy {

    @Override
    public Declaration parse(Token[] tokens, ParsingContext ctx) {
        // The token indexes are assured by the tokenizer, therefore we can index it.
        Identifier identifier = new Identifier(tokens[1].literal());

        // A state can take on multiple forms, therefore we delegate the
        // creation logic to a state factory.
        State declaration = StateFactory.create(tokens, ctx);

        ctx.addState(identifier, declaration);

        return declaration;
    }
}
