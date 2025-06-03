package nl.avans.parser;

import nl.avans.declaration.Declaration;
import nl.avans.declaration.Transition;
import nl.avans.tokenizer.Token;

public class TransitionParser implements DeclarationParserStrategy {

    @Override
    public Declaration parse(Token[] tokens, ParsingContext ctx) {
        String identifier = tokens[1].literal();
        String sourceIdentifier = tokens[2].literal();
        String destinationIdentifier = tokens[3].literal();
        String triggerIdentifier = tokens[4].literal();
        String guardCondition = tokens[5].literal();

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
