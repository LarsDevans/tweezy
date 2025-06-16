package nl.avans.parser;

import nl.avans.declaration.Declaration;
import nl.avans.declaration.Trigger;
import nl.avans.declaration.Declaration.Identifier;
import nl.avans.declaration.Trigger.TriggerDescription;
import nl.avans.tokenizer.Token;

public class TriggerParser implements DeclarationParserStrategy {

    @Override
    public Declaration parse(Token[] tokens, ParsingContext ctx) {
        // The token indexes are assured by the tokenizer, therefore we can index it.
        Identifier identifier = new Identifier(tokens[1].literal());
        TriggerDescription description = new TriggerDescription(tokens[2].literal());

        Trigger declaration = new Trigger(identifier, description);

        ctx.addTrigger(identifier, declaration);

        return declaration;
    }
}
