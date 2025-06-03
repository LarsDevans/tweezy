package nl.avans.parser;

import nl.avans.declaration.Action;
import nl.avans.declaration.Declaration;
import nl.avans.tokenizer.Token;

public class ActionParser implements DeclarationParserStrategy {

    @Override
    public Declaration parse(Token[] tokens, ParsingContext ctx) {
        String identifier = tokens[1].literal();
        String description = tokens[2].literal();
        String actionType = tokens[3].literal();

        Action declaration = new Action(identifier, description, actionType);

        ctx.addAction(identifier, declaration);

        return declaration;
    }

}
