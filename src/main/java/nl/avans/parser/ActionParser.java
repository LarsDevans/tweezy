package nl.avans.parser;

import nl.avans.declaration.Action;
import nl.avans.declaration.Declaration;
import nl.avans.declaration.Action.ActionDescription;
import nl.avans.declaration.Action.ActionType;
import nl.avans.declaration.Declaration.Identifier;
import nl.avans.tokenizer.Token;

public class ActionParser implements DeclarationParserStrategy {

    @Override
    public Declaration parse(Token[] tokens, ParsingContext ctx) {
        // The token indexes are assured by the tokenizer, therefore we can index it.
        Identifier identifier = new Identifier(tokens[1].literal());
        ActionDescription description = new ActionDescription(tokens[2].literal());
        ActionType actionType = ActionType.valueOf(tokens[3].literal());

        Action declaration = new Action(identifier, description, actionType);

        ctx.addAction(identifier, declaration);

        return declaration;
    }
}
