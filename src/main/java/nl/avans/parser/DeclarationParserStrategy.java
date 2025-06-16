package nl.avans.parser;

import nl.avans.declaration.Declaration;
import nl.avans.tokenizer.Token;

// Turns the raw tokens into a their respective concrete declaration.
public interface DeclarationParserStrategy {

    public abstract Declaration parse(Token[] tokens, ParsingContext ctx);

}
