package nl.avans.parser;

import nl.avans.declaration.Declaration;
import nl.avans.declaration.Trigger;
import nl.avans.tokenizer.Token;

public class TriggerParser implements DeclarationParserStrategy {

    @Override
    public Declaration parse(Token[] tokens, ParsingContext ctx) {
        String identifier = tokens[1].literal();
        String description = tokens[2].literal();

        Trigger declaration = new Trigger(identifier, description);

        ctx.addTrigger(identifier, declaration);

        return declaration;
    }

}
