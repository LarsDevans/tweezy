package nl.avans.parser;

import nl.avans.declaration.Declaration;
import nl.avans.declaration.Transition;
import nl.avans.declaration.Declaration.Identifier;
import nl.avans.declaration.Transition.GuardCondition;
import nl.avans.tokenizer.Token;

public class TransitionParser implements DeclarationParserStrategy {

    @Override
    public Declaration parse(Token[] tokens, ParsingContext ctx) {
        // The token indexes are assured by the tokenizer, therefore we can index it.
        Identifier identifier = new Identifier(tokens[1].literal());
        Identifier sourceIdentifier = new Identifier(tokens[2].literal());
        Identifier destinationIdentifier = new Identifier(tokens[3].literal());
        Identifier triggerIdentifier = new Identifier(tokens[4].literal());
        GuardCondition guardCondition = new GuardCondition(tokens[5].literal());

        Transition declaration = new Transition(
            identifier,
            sourceIdentifier,
            destinationIdentifier,
            triggerIdentifier,
            guardCondition
        );

        ctx.addTransition(identifier, declaration);

        return declaration;
    }
}
